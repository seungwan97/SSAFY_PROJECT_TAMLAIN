import numpy as np
from flask import Flask, request, json, Response
import pandas as pd
from surprise import KNNBaseline, Reader
from mlxtend.frequent_patterns.fpgrowth import fpgrowth
from mlxtend.preprocessing import TransactionEncoder
from mlxtend.frequent_patterns import association_rules
from surprise.dataset import DatasetAutoFolds

app = Flask(__name__)

@app.route('/test', methods=['POST'])
def getFirstRecommend():
    # print(request.is_json)
    data = request.get_json()
    user_id = data['flaskSurveyReq']['userId']
    theme_id = data['flaskSurveyReq']['travelThemeCode']
    season_name = data['flaskSurveyReq']['season']
    selected_category = data['flaskSurveyReq']['surveyFavorCategoryList']
    place = pd.DataFrame.from_dict(data['flaskJejuPlaceItemList'])
    ratings = pd.DataFrame.from_dict(data['flaskReviewItemList'])

    place['reviewScore'] = place.apply(
        lambda x: x['reviewScoreSum'] / x['reviewCount'] if x['reviewCount'] != 0 else 0, axis=1)
    place['reviewReliability'] = pd.qcut(place['reviewCount'], q=4,
                                         labels=[score / 10 for score in range(7, 11)]).astype(np.float32)
    place['score'] = place['reviewScore'] * place['reviewReliability']
    place = place.sort_values('score', ascending=False)
    category = place["categoryName"].unique().tolist()
    grouped_place_cbf = place.groupby('categoryName')['jejuPlaceId'].apply(list)

    ratings["itemId"] = ratings.apply(lambda x: '{}_{}_{}'.format(x.season, x.travelThemeCode, x.jejuPlaceId), axis=1)
    # courseList = ratings.groupby(['scheduleId', 'day'])['jejuPlaceId'].apply(list).reset_index(
    #     name='visitedPlaceList')
    ratings.drop_duplicates(['userId', 'itemId'], keep='last') # 같은 계절&테마로 방문한 장소의 리뷰 처리
    ratings = pd.merge(ratings, place[["jejuPlaceId", "categoryId", "categoryName"]], left_on='jejuPlaceId', right_on='jejuPlaceId', how='left')
    reader = Reader(rating_scale=(0, 5))
    print(ratings)
    data_folds = DatasetAutoFolds(df=ratings[["userId", "itemId", "score"]], reader=reader)
    trainset = data_folds.build_full_trainset()
    sim_options = {'name': 'cosine', 'user_based': True}
    algo = KNNBaseline(sim_options=sim_options)
    algo.fit(trainset)
    item_list = ratings[ratings["categoryId"].isin(selected_category)]["itemId"].unique().tolist()
    predictions = [algo.predict(user_id, item_id) for item_id in item_list]
    rating_prediction = pd.DataFrame()
    rating_prediction["itemId"] = item_list
    rating_prediction["score"] = predictions
    rating_prediction = place.sort_values('score', ascending=False)
    grouped_place_cf = rating_prediction.groupby('categoryName')['jejuPlaceId'].apply(list)

    result = {}
    result_size = 20
    for name in category:
        if len(grouped_place_cf[name]) > result_size:
            result[name] = grouped_place_cf[name][:result_size]
        else: result[name] = grouped_place_cbf[name][:result_size]

    return result


@app.route('/recommend', methods=['POST'])
def getReRecommend():
    # print(request.is_json)
    data = request.get_json()
    user_id = data['flaskSurveyReq']['userId']
    theme_id = data['flaskSurveyReq']['travelThemeCode']
    season_name = data['flaskSurveyReq']['season']
    selected_category = data['flaskSurveyReq']['surveyFavorCategoryList']
    place = pd.DataFrame.from_dict(data['flaskJejuPlaceItemList'])
    ratings = pd.DataFrame.from_dict(data['flaskReviewItemList'])
    selected_place = []

    if selected_place:
        # confidence 데이터 : 코스에 장소 A가 있을때 장소 B도 있을 조건부 확률
        courseList = ratings[ratings["season"] == season_name and ratings["travelThemeCode"] == theme_id].groupby(['scheduleId', 'day'])['jejuPlaceId'].apply(list)
        my_transactionencoder = TransactionEncoder()  # One-Hot Encoding 된 DataFrame
        my_transactionencoder.fit(courseList)
        encoded_transactions = my_transactionencoder.transform(courseList)

        encoded_transactions_df = pd.DataFrame(encoded_transactions, columns=my_transactionencoder.columns_)

        min_support = 5 / len(courseList)  # 아이템 조합의 최소 지지도 설정 (최소 5번 출현한 장소)
        frequent_itemsets = fpgrowth(encoded_transactions_df, min_support=min_support, use_colnames=True, max_len=2)
        rules = association_rules(frequent_itemsets, metric="confidence", min_threshold=0.1) # 최소 신뢰도 0.1

        max_confidence = rules["confidence"].max()
        min_confidence = rules["confidence"].min()
        rules["confidence_score"] = rules.apply(lambda x: (x["confidence"]-min_confidence)/(max_confidence-min_confidence)*5)
        rules = pd.merge(rules, place[["jejuPlaceId", "categoryName", "categoryId"]], left_on='consequents',
                              right_on='jejuPlaceId')
        rules.sort_values(['confidence'], ascending=False).reset_index(drop=True)
        selected_antecedents = [frozenset({i}) for i in selected_place]
        confidence = rules[rules['antecedents'].isin(selected_antecedents)].sort_values(by='confidence', ascending=False)

    print(confidence.info())

    place['reviewScore'] = place.apply(
        lambda x: x['reviewScoreSum'] / x['reviewCount'] if x['reviewCount'] != 0 else 0, axis=1)
    place['reviewReliability'] = pd.qcut(place['reviewCount'], q=4,
                                         labels=[score / 10 for score in range(7, 11)]).astype(np.float32)
    place['score'] = place['reviewScore'] * place['reviewReliability']
    place = place.sort_values('score', ascending=False)
    category = place["categoryName"].unique().tolist()
    grouped_place_cbf = place.groupby('categoryName')['jejuPlaceId'].apply(list)

    # ratings["itemId"] = ratings.apply(lambda x: '{}_{}_{}'.format(x.season, x.travelThemeCode, x.jejuPlaceId), axis=1)
    # ratings.drop_duplicates(['userId', 'itemId'], keep='last')  # 같은 계절&테마로 방문한 장소의 리뷰 처리
    ratings.drop_duplicates(['userId', 'jejuPlaceId'], keep='last')

    min_visit_ratings = 5
    ratings = ratings.value_counts() >= min_visit_ratings # 최소 5개의 리뷰를 작성한 사용자를 대상으로 협업 필터링 진행
    ratings = pd.merge(ratings, place[["jejuPlaceId", "categoryId", "categoryName"]], left_on='jejuPlaceId',
                       right_on='jejuPlaceId', how='left')
    reader = Reader(rating_scale=(0, 5))
    print(ratings)
    # data_folds = DatasetAutoFolds(df=ratings[["userId", "itemId", "score"]], reader=reader)
    data_folds = DatasetAutoFolds(df=ratings[["userId", "jejuPlaceId", "score"]], reader=reader)
    trainset = data_folds.build_full_trainset()
    sim_options = {'name': 'cosine', 'user_based': True}
    algo = KNNBaseline(sim_options=sim_options)
    algo.fit(trainset)
    # item_list = ratings[ratings["categoryId"].isin(selected_category)]["itemId"].unique().tolist()
    item_list = ratings[ratings["categoryId"].isin(selected_category)]["jejuPlaceId"].unique().tolist()
    predictions = [algo.predict(user_id, item_id) for item_id in item_list]
    rating_prediction = pd.DataFrame()
    # rating_prediction["itemId"] = item_list
    rating_prediction["jejuPlaceId"] = item_list
    rating_prediction["prediction"] = predictions
    rating_prediction = place.sort_values('prediction', ascending=False)
    grouped_place_cf = rating_prediction.groupby('categoryName')['jejuPlaceId'].apply(list)

    result = {}
    result_size = 20
    for name in category:
        if len(grouped_place_cf[name]) > result_size:
            result[name] = grouped_place_cf[name][:result_size]
        else:
            result[name] = grouped_place_cbf[name][:result_size]
    print(result)


if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)
