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

@app.route('/recommend', methods=['POST'])
def getFirstRecommend():
    data = request.get_json()
    user_id = data['flaskSurveyItem']['userId']
    theme_id = data['flaskSurveyItem']['travelThemeCode']
    season_name = data['flaskSurveyItem']['season']
    selected_category = data['flaskSurveyItem']['surveyFavorCategoryList']
    place = pd.DataFrame.from_dict(data['flaskJejuPlaceItemList'])
    ratings = pd.DataFrame.from_dict(data['flaskReviewItemList'])
    # 에러남
    # min_visit_ratings = 0
    # ratings = ratings.drop(ratings[ratings["userId"].value_counts() > min_visit_ratings])
    print(ratings)
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
    print(coo_matrix((visited_flag, (ratings["jejuPlaceId"], ratings["scheduleId"]))))
    visited_mat = coo_matrix((visited_flag, (ratings["scheduleId"], ratings["jejuPlaceId"]))).toarray()
    print(visited_mat)
    selected_max = np.zeros(len(visited_mat[0]), dtype=np.int8)
    for idx in selected_place: selected_max[idx] = 1
    print("selected_place", selected_place)
    visited_mat = visited_mat[:, selected_place].tolist()
    my_course = np.ones(len(selected_place), dtype=np.int8)
    schedule["survey_similarity"] = schedule.apply(
        lambda x: 1.0 if x['travelThemeCode'] == theme_id and x['season'] == season_name else
        (0.3 if x['travelThemeCode'] == theme_id or x['season'] == season_name else 0), axis=1)
    if selected_place:
        schedule["vec"] = schedule["scheduleId"].apply(lambda x: visited_mat[x])
        schedule["course_similarity"] = schedule.apply(lambda x: cosine_similarity(schedule["vec"], my_course), axis=1)
        schedule = schedule[schedule["course_similarity"] > 0 and schedule["survey_similarity"] > 0]
        schedule["similarity"] = schedule["course_similarity"] + schedule["survey_similarity"]
        schedule = schedule[schedule["similarity"] > 0.5]
    else :
        schedule["similarity"] = schedule["survey_similarity"]
        schedule = schedule[schedule["similarity"] > 0]

    grouped_by_similarity = schedule.groupby('similarity')['course'].apply(list).reset_index(name='visited_place')
    grouped_by_similarity = grouped_by_similarity.sort_values('similarity', ascending=True)
    print(grouped_by_similarity)
    grouped_by_similarity["unique_visited_place"] = grouped_by_similarity["visited_place"].apply(lambda x: Counter(sum(x, [])))
    # schedule = place.sort_values('similarity', ascending=False)
    grouped_by_similarity["unique_visited_place"] = grouped_by_similarity["unique_visited_place"].apply(lambda x: sorted(x, key=x.get, reverse=True))
    top_recommend_place_id = []
    for place_id_list in grouped_by_similarity["unique_visited_place"].values:
        for id in place_id_list:
            if id not in top_recommend_place_id:
                top_recommend_place_id.append(id)
    ranked_place = pd.DataFrame()
    for id in top_recommend_place_id:
        ranked_place = pd.concat([ranked_place, place[place["jejuPlaceId"] == id]])
        place = place.drop(place[place["jejuPlaceId"] == id].index)

    reader = Reader(rating_scale=(0, 5))
    data_folds = DatasetAutoFolds(df=ratings[["userId", "jejuPlaceId", "score"]], reader=reader)
    trainset = data_folds.build_full_trainset()
    sim_options = {'name': 'cosine', 'user_based': True}
    algo = KNNBaseline(sim_options=sim_options)
    algo.fit(trainset)
    item_list = ratings[ratings["categoryId"].isin(selected_category)]["jejuPlaceId"].unique().tolist()
    predictions = [algo.predict(user_id, item_id) for item_id in item_list]
    if predictions:
        predictions = pd.DataFrame(predictions)  # uid, iid, r_ui(실제 평점) ,est(예측평점)
        print(predictions)
        place = pd.merge(place, predictions, left_on='jejuPlaceId', right_on='iid')
        place["prediction"] = place["score"] + place["est"]
    else :
        place["prediction"] = place["score"]
    place = place.sort_values('prediction', ascending=False)
    ranked_place = pd.concat([ranked_place, place])

    result = {}
    result_size = 20
    grouped_place = ranked_place.groupby('categoryName')['jejuPlaceId'].apply(list)
    for name in category:
        result[name] = grouped_place[name][:result_size]
    print(result)
    return result


if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)
