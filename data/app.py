import numpy as np
from flask import Flask, request, json, Response
import pandas as pd
from surprise import KNNBaseline, Reader
from mlxtend.frequent_patterns.fpgrowth import fpgrowth
from mlxtend.preprocessing import TransactionEncoder
from mlxtend.frequent_patterns import association_rules
from surprise.dataset import DatasetAutoFolds
from scipy.sparse import coo_matrix
from collections import Counter

app = Flask(__name__)

def cosine_similarity(v1, v2):
    dot_product = np.dot(v1, v2.T)
    norm_v1 = np.linalg.norm(v1)
    norm_v2 = np.linalg.norm(v2)
    if norm_v1 == 0 or norm_v2 == 0:
        return 0
    else:
        return dot_product / (norm_v1 * norm_v2)

@app.route('/test', methods=['POST'])
def getFirstRecommend():
    data = request.get_json()
    user_id = data['flaskSurveyReq']['userId']
    theme_id = data['flaskSurveyReq']['travelThemeCode']
    season_name = data['flaskSurveyReq']['season']
    selected_category = data['flaskSurveyReq']['surveyFavorCategoryList']
    place = pd.DataFrame.from_dict(data['flaskJejuPlaceItemList'])
    ratings = pd.DataFrame.from_dict(data['flaskReviewItemList'])
    min_visit_ratings = 5
    ratings = ratings["userId"].value_counts() >= min_visit_ratings
    schedule = ratings.groupby(['scheduleId', 'travelThemeCode', 'season'])['jejuPlaceId'].apply(list).reset_index(
        name='course')
    # courseList = courseList[set(selected_place).intersection(courseList["course"])] # 겹치는 장소가 있는 곳만
    ratings = ratings.drop_duplicates(['userId', 'jejuPlaceId'], keep='last')
    selected_place = []

    place['reviewScore'] = place.apply(
        lambda x: x['reviewScoreSum'] / x['reviewCount'] if x['reviewCount'] != 0 else 0, axis=1)
    place['reviewReliability'] = pd.qcut(place['reviewCount'], q=4,
                                         labels=[score / 10 for score in range(7, 11)]).astype(np.float32)
    place['score'] = place['reviewScore'] * place['reviewReliability']
    place = place.sort_values('score', ascending=False)
    category = place["categoryName"].unique().tolist()
    visited_flag = np.ones(len(ratings), dtype=np.int8)
    visited_mat = coo_matrix((visited_flag, (ratings["jejuPlaceId"], ratings["scheduleId"])))
    selected_max = np.zeros(len(visited_mat[0]), dtype=np.int8)
    for idx in selected_place: selected_max[idx] = 1
    all_course = visited_mat[:, selected_place].tolist()
    my_course = np.ones(len(selected_place), dtype=np.int8).tolist()

    schedule["course_similarity"] = schedule.apply(lambda x: cosine_similarity(all_course[schedule["jejuPlaceId"]], my_course), axis=1)
    schedule["survey_similarity"] = schedule.apply(lambda x: 1.0 if x['travelThemeCode'] == theme_id and x['season'] == season_name else
    (0.3 if x['travelThemeCode'] == theme_id or x['season'] == season_name else 0), axis=1)

    schedule = schedule[schedule["course_similarity"] > 0 and schedule["survey_similarity"] > 0]
    schedule["similarity"] = schedule["course_similarity"] + schedule["survey_similarity"]
    schedule = schedule[schedule["similarity"] > 0.5]
    grouped_by_similarity = schedule.groupby('similarity')['jejuPlaceId'].apply(list).reset_index(name='visited_place')
    grouped_by_similarity["unique_visited_place"] = grouped_by_similarity["similarity"].apply(lambda x: Counter(sum(x["visited_place"], [])))
    # schedule = place.sort_values('similarity', ascending=False)
    grouped_by_similarity["unique_visited_place"] = grouped_by_similarity["unique_visited_place"].apply(lambda x: sorted(x, key=x.get, reverse=True))
    top_recommend_place_id = []
    for place_id_list in grouped_by_similarity["unique_visited_place"].values:
        for id in place_id_list:
            if id not in top_recommend_place_id:
                top_recommend_place_id.append(id)
    ranked_place = pd.DataFrame()
    for id in top_recommend_place_id:
        ranked_place = pd.concat(ranked_place, place[place["jejuPlaceId"] == id])
        place = place.drop(place[place["jejuPlaceId"] == id].index)

    reader = Reader(rating_scale=(0, 5))
    data_folds = DatasetAutoFolds(df=ratings[["userId", "jejuPlaceId", "score"]], reader=reader)
    trainset = data_folds.build_full_trainset()
    sim_options = {'name': 'cosine', 'user_based': True}
    algo = KNNBaseline(sim_options=sim_options)
    algo.fit(trainset)
    item_list = ratings[ratings["categoryId"].isin(selected_category)]["jejuPlaceId"].unique().tolist()
    predictions = [algo.predict(user_id, item_id) for item_id in item_list]
    predictions = pd.DataFrame(predictions)  # uid, iid, r_ui(실제 평점) ,est(예측평점)
    place = pd.merge(place, predictions, left_on='jejuPlaceId', right_on='iid')
    place["prediction"] = place["score"] + place["est"] + place["confidence"]
    place = place.sort_values('prediction', ascending=False)
    ranked_place = pd.concat(ranked_place, place)

    result = {}
    result_size = 20
    grouped_place = ranked_place.groupby('categoryName')['jejuPlaceId'].apply(list)
    for name in category:
        result[name] = grouped_place[name][:result_size]
    print(result)
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

    place['reviewScore'] = place.apply(
        lambda x: x['reviewScoreSum'] / x['reviewCount'] if x['reviewCount'] != 0 else 0, axis=1)
    place['reviewReliability'] = pd.qcut(place['reviewCount'], q=4,
                                         labels=[score / 10 for score in range(7, 11)]).astype(np.float32)
    place['score'] = place['reviewScore'] * place['reviewReliability']
    place = place.sort_values('score', ascending=False)
    category = place["categoryName"].unique().tolist()
    # grouped_place_cbf = place.groupby('categoryName')['jejuPlaceId'].apply(list)

    # confidence 데이터 : 코스에 장소 A가 있을때 장소 B도 있을 조건부 확률
    courseList = ratings[ratings["season"] == season_name and ratings["travelThemeCode"] == theme_id].groupby(['scheduleId', 'day'])[
        'jejuPlaceId'].apply(list)
    my_transactionencoder = TransactionEncoder()  # One-Hot Encoding 된 DataFrame
    my_transactionencoder.fit(courseList)
    encoded_transactions = my_transactionencoder.transform(courseList)

    encoded_transactions_df = pd.DataFrame(encoded_transactions, columns=my_transactionencoder.columns_)

    min_support = 5 / len(courseList)  # 아이템 조합의 최소 지지도 설정 (최소 5번 출현한 장소)
    frequent_itemsets = fpgrowth(encoded_transactions_df, min_support=min_support, use_colnames=True, max_len=2)
    rules = association_rules(frequent_itemsets, metric="confidence", min_threshold=0.1)  # 최소 신뢰도 0.1

    max_confidence = rules["confidence"].max()
    min_confidence = rules["confidence"].min()
    min_confidence = min_confidence - (max_confidence - min_confidence) / 4
    rules["confidence_score"] = rules.apply(
        lambda x: (x["confidence"] - min_confidence) / (max_confidence - min_confidence) * 5)
    rules["from"] = rules.apply(lambda x: list(x["antecedents"])[0])
    rules["to"] = rules.apply(lambda x: list(x["consequents"])[0])
    # rules = pd.merge(rules, place["jejuPlaceId"], left_on='to', right_on='jejuPlaceId')
    # rules = rules.sort_values(['confidence'], ascending=False).reset_index(drop=True)
    if selected_place:
        rules = rules[rules['from'].isin(selected_place)]
    place = pd.merge(place, rules, left_on='jejuPlaceId', right_on='to')

    # ratings["itemId"] = ratings.apply(lambda x: '{}_{}_{}'.format(x.season, x.travelThemeCode, x.jejuPlaceId), axis=1)
    # ratings.drop_duplicates(['userId', 'itemId'], keep='last')  # 같은 계절&테마로 방문한 장소의 리뷰 처리
    ratings.drop_duplicates(['userId', 'jejuPlaceId'], keep='last')

    min_visit_ratings = 5
    ratings = ratings["userId"].value_counts() >= min_visit_ratings # 최소 5개의 리뷰를 작성한 사용자를 대상으로 협업 필터링 진행
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
    predictions = pd.DataFrame(predictions) # uid, iid, r_ui(실제 평점) ,est(예측평점)
    # rating_prediction["itemId"] = item_list
    place = pd.merge(place, predictions, left_on='jejuPlaceId',right_on='iid')
    # rating_prediction = place.sort_values('prediction', ascending=False)
    # grouped_place_cf = rating_prediction.groupby('categoryName')['jejuPlaceId'].apply(list)

    result = {}
    result_size = 20
    place["prediction"] = place["score"] + place["est"] + place["confidence"]
    place = place.sort_values('prediction', ascending=False)
    grouped_place = place.groupby('categoryName')['jejuPlaceId'].apply(list)
    for name in category:
            result[name] = grouped_place[name][:result_size]
    print(result)


if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)
