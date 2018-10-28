#-*- coding:utf-8 -*-
import json
import sys
import logging
import rds_config
import pymysql
import pandas as pd
import numpy as np
#rds settings
rds_host  = "fooddb.crpamqesvgcc.us-east-2.rds.amazonaws.com"
name = rds_config.db_username
password = rds_config.db_password
db_name = rds_config.db_name


logger = logging.getLogger()
logger.setLevel(logging.INFO)

try:
    conn = pymysql.connect(rds_host, user=name, passwd=password, db=db_name, connect_timeout=5)
except:
    logger.error("ERROR: Unexpected error: Could not connect to MySql instance.")
    sys.exit()

logger.info("SUCCESS: Connection to RDS mysql instance succeeded")
def handler(event, context):
    """
    This function fetches content from mysql RDS instance


    """



    item_count = 0



    input = pd.read_sql_query("select * from Food where Name ='"+event['a']+"';", conn)
    output = pd.read_sql_query("select * from Food where Country ='"+event['b']+"';", conn)

    # add another column 'score'
    output['score'] = 0

    input_name = input['Name']
    input_URL = input['ImageURL']
    input_detail = input['detail']
    input_category = input['Category']
    input_category2 = input['Category2']
    input_main1 = input['MainIngredient1']
    input_main2 = input['MainIngredient2']
    input_main3 = input['MainIngredient3']
    input_sub1 = input['SubIngredient1']
    output_name = output['Name']
    output_detail = output['detail']
    output_URL = output['ImageURL']
    output_category = output['Category']
    output_main1 = output['MainIngredient1']
    output_main2 = output['MainIngredient2']
    output_main3 = output['MainIngredient3']
    output_sub1 = output['SubIngredient1']
    temp = []
    temp2 = []  # type: List[Any]

    print(input)
    print(output)

    ##Categorizing
    for i in input_category:
        for j in output_category:
            if i == j:
                temp = np.where(output_category != j)
    if len(temp) > 0:
        categorized = output.drop(temp[0], 0)
        categorized = categorized.reset_index(drop=True)

    print(categorized)

    temp = []
    temp2 = []
    ##Category 2
    if input_category2 != None:
        for i in input_category2:
            for j in categorized['Category2']:
                if i == j:
                    temp = np.where(categorized['Category2'] == i)
                else:
                    temp2 = np.where(categorized['Category2'] != i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 10

    temp = []
    temp2 = []

    ##Main1
    for i in input_main1:
        for j in categorized['MainIngredient1']:
            if i == j:
                temp = np.where(categorized['MainIngredient1'] == i)
            else:
                temp2 = np.where(categorized['MainIngredient1'] != i)

    if len(temp)>0 or len(temp2)>0:
        categorized['score'][temp[0]] += 30
        categorized['score'][temp2[0]] += 5

    print(categorized)

    temp = []
    temp2 = []

    ##Main2
    for i in input_main2:
        for j in categorized['MainIngredient2']:
            if i == j:
                temp = np.where(categorized['MainIngredient2'] == i)
            else:
                temp2 = np.where(categorized['MainIngredient2'] != i)

    print(temp)
    print(temp2)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 20
    if len(temp2) > 0:
        categorized['score'][temp2[0]] += 5

    print('MAIN2222')
    print(input)
    print(categorized)

    temp = []
    temp2 = []

    ##Main2 & 3 comparing
    for i in input_main2:
        for j in categorized['MainIngredient3']:
            if i == j:
                temp = np.where(categorized['MainIngredient3'] == i)
            else:
                temp2 = np.where(categorized['MainIngredient3'] != i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 7

    print(temp)
    print(temp2)
    print('MAIN23')
    print(input)
    print(categorized)

    temp = []
    temp2 = []

    ##Main3
    for i in input_main3:
        for j in categorized['MainIngredient3']:
            if i == j:
                temp = np.where(categorized['MainIngredient3'] == i)
            else:
                temp2 = np.where(categorized['MainIngredient3'] != i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 10
    if len(temp2) > 0:
        categorized['score'][temp2[0]] += 5
    print('MAIN333')
    print(input)
    print(categorized)

    temp = []
    temp2 = []

    ##Main3 & 2 comparing
    for i in input_main3:
        for j in categorized['MainIngredient2']:
            if i == j:
                temp = np.where(categorized['MainIngredient2'] == i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 4

    temp = []
    temp2 = []

    ##Sub1
    for i in input_sub1:
        for j in categorized['SubIngredient1']:
            if i == j:
                temp = np.where(categorized['SubIngredient1'] == i)
            else:
                temp2 = np.where(categorized['SubIngredient1'] != i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 10

    print('SUB1')
    print(input)
    print(categorized)

    for i in categorized['score']:
        if i < 15:
            temp = np.where(categorized['score'] == i)
    if len(temp) > 0:
        categorized = categorized.drop(temp[0], 0)
        categorized = categorized.reset_index(drop=True)



    categorized = categorized.sort_values(["score"], ascending=[False])
    categorized = categorized.reset_index(drop=True)

    print('sort')
    print(categorized)
    print('')
    print('########RESULT########')
    print(categorized['Name'])



    return {
        "statusCode": 200,
        "ImageURL" : categorized['ImageURL'][0],
        "CategoryID": categorized['Category'][0],
        "Name" : categorized['Name'][0],
        "Main Ingredient1" : categorized['MainIngredient1'][0],
        "Main Ingredient2" : categorized['MainIngredient2'][0],
        "Main Ingredient3" : categorized['MainIngredient3'][0],
        "Sub Ingredient1"  : categorized['SubIngredient1'][0],
        "detail": categorized['detail'][0]
    }
