<<<<<<< HEAD
import numpy as np
from flask import Flask, request, json, Response
import pandas as pd
from surprise import KNNBaseline, Reader, dump
from mlxtend.frequent_patterns.fpgrowth import fpgrowth
from mlxtend.preprocessing import TransactionEncoder
from mlxtend.frequent_patterns import association_rules
from surprise.dataset import DatasetAutoFolds

app = Flask(__name__)

@app.route('/firstRecommend', methods=['POST'])
def getFirstRecommend():
    print(request.is_json)
    data = request.get_json()

    # 장소 데이터 : flaskJejuPlaceItemList(jejuPlaceId, categoryId, subCategoryId, reviewScoreSum, reviewCount)
    place = pd.DataFrame.from_dict(data['flaskJejuPlaceItemList'])
    place['reviewScore'] = place.apply(
        lambda x: x['reviewScoreSum'] / x['reviewCount'] if x['reviewCount'] != 0 else 0, axis=1)
    place['reviewReliability'] = pd.qcut(place['reviewCount'], q=5,
                                         labels=[score / 10 for score in range(5, 11)]).astype(np.float32)
    place['score'] = place['reviewScore'] * place['reviewReliability']
    place = place.sort_values('score', ascending=False)

    category = place["categoryName"].unique().tolist()
    grouped_place = place.groupby('categoryName')['jejuPlaceId'].apply(list)
    result = {}
    for name in category:
        result[name] = grouped_place[name][:20]

    return result


    # result = json.dumps(function(data))
    # res = Response(result, mimetype="application/json")
    # return res


@app.route('/reRecommend', methods=['POST'])
def getReRecommend():
    print(request.is_json)
    data = request.get_json()
    userId = ''
    theme_id = data['surveyRegistReq']['travelThemeCode']
    season_id = data['surveyRegistReq']['season']
    selected_category = data['surveyRegistReq']['surveyFavorCategoryList']
    selected_place = data['scheduleItemMap']

    # 장소 데이터 : flaskJejuPlaceItemList(jejuPlaceId, categoryId, subCategoryId, reviewScoreSum, reviewCount)
    place = pd.DataFrame.from_dict(data['flaskJejuPlaceItemList'])
    place['reviewScore'] = place.apply(
        lambda x: x['reviewScoreSum'] / x['reviewCount'] if x['reviewCount'] != 0 else 0, axis=1)
    place['reviewReliability'] = pd.qcut(place['reviewCount'], q=5,
                                         labels=[score / 10 for score in range(5, 11)]).astype(np.float32)
    place['score'] = place['reviewScore'] * place['reviewReliability']
    place = place[["jejuPlaceId", "categoryId", "subCategoryId", "score"]]

    # 평점 데이터 : 유저, 아이템(계절, 테마, 장소), 평점 (+ 일정, 일차)
    ratings = pd.DataFrame.from_dict(data['reviewItems'])
    visitedPlaceList = ratings.groupby(['scheduleId', 'day'])['jejuPlaceId'].apply(list).reset_index(
        name='visitedPlaceList')
    ratings["itemId"] = '_'.join([ratings["season"], ratings["travelThemeCode"], ratings["jejuPlaceId"]])
    ratings = ratings[["userId", "itemId", "reviewScore"]]

    # confidence 데이터 : 코스에 장소 A가 있을때 장소 B도 있을 조건부 확률
    my_transactionencoder = TransactionEncoder()  # One-Hot Encoding 된 DataFrame
    my_transactionencoder.fit(visitedPlaceList)
    encoded_transactions = my_transactionencoder.transform(visitedPlaceList)

    encoded_transactions_df = pd.DataFrame(encoded_transactions, columns=my_transactionencoder.columns_)

    min_support = 5 / len(visitedPlaceList)  # 아이템 조합의 최소 지지도 설정 (최소 5번 출현한 장소)
    frequent_itemsets = fpgrowth(encoded_transactions_df, min_support=min_support, use_colnames=True, max_len=2)
    confidence = association_rules(frequent_itemsets, metric="confidence", min_threshold=0.01)
    confidence = pd.merge(confidence, place[["jejuPlaceId", "categoryId", "subCategoryId"]], left_on='consequents',
                          right_on='jejuPlaceId')

    reader = Reader(rating_scale=(0, 5))
    data_folds = DatasetAutoFolds(df=ratings[["userId", "itemId", "reviewScore"]], reader=reader)
    trainset = data_folds.build_full_trainset()
    sim_options = {'name': 'cosine', 'user_based': True}
    algo = KNNBaseline(sim_options=sim_options)
    algo.fit(trainset)



    place = pd.read_pickle('place.pkl')
    ratings = pd.read_pickle('ratings.pkl')
    confidence = pd.read_pickle('confidence.pkl')

    confidence = confidence[confidence["antecedents"].isin(selected_place)]
    confidence = confidence[confidence["subCategoryId"].isin(selected_category)]
    if len(confidence) > 20: # CBF +CF
        return []
    else : # CBF
        place = place[place['subCategoryId'].isin(selected_category)]
        place = place[~place['jejuPlaceId'].isin(selected_place)]
        return place.sort_values('score', ascending=False)[:20]

    # result = json.dumps(function(data))
    # res = Response(result, mimetype="application/json")
    # return res


if __name__ == '__main__':
    app.run(host='0.0.0.0')
