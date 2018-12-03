#-*- coding:utf-8 -*-
import json
import sys
import logging
import rds_config
import pymysql
import pandas as pd
import numpy as np
#rds settings
rds_host  = "fooddb2.crpamqesvgcc.us-east-2.rds.amazonaws.com"
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



    input = pd.read_sql_query("select * from Food where Name ='"+event['a']+"' and Country ='"+event['c']+"';", conn)


    # add another column 'score'


    input_name = input['Name']
    input_URL = input['ImageURL']
    input_detail = input['detail']
    input_category = input['Category']
    input_cookery = input['Cookery']
    input_main1 = input['MainIngredient1']
    input_main2 = input['MainIngredient2']
    input_main3 = input['MainIngredient3']
    input_sub1 = input['SubIngredient1']
    input_subcategory = input['SubCategory']

    if len(input) == 0:
        return {
                "statusCode": 200,
                "length":0
                
            
        

                }
    output = pd.read_sql_query("select * from Food where Country ='" + event['b'] + "'and Category ='"+ input_category[0] +"';", conn)

    output['score'] = 0
    categorized = output



    ##Cookery
    if input_cookery is not None:
        for i in input_cookery:
            temp = np.where(categorized["Cookery"] == i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 20

    ##SubCategory
    if input_subcategory is not None:
        for i in input_subcategory:
            temp = np.where(categorized['SubCategory'] == i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 20

    #####Main1 Comparing####
    ##Main1 & 1 comparing
    for i in input_main1:
        temp = np.where(categorized['MainIngredient1'] == i)
        temp2 = np.where(categorized['MainIngredient1'] != i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 20

    ##Main1 & 2 comparing
    for i in input_main1:
        temp = np.where(categorized['MainIngredient2'] == i)
        temp2 = np.where(categorized['MainIngredient2'] != i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 13

    ##Main1 & 3 comparing
    for i in input_main1:
        temp = np.where(categorized['MainIngredient3'] == i)
        temp2 = np.where(categorized['MainIngredient3'] != i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 13

    ####Main2 Comparing###
    ##Main2 & 2 comparing

    for i in input_main2:
        temp = np.where(categorized['MainIngredient2'] == i)
        temp2 = np.where(categorized['MainIngredient2'] != i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 13

    ##Main2 & 1 comparing
    for i in input_main2:
        temp = np.where(categorized['MainIngredient1'] == i)
        temp2 = np.where(categorized['MainIngredient1'] != i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 8

    ##Main2 & 3 comparing
    for i in input_main2:
        temp = np.where(categorized['MainIngredient3'] == i)
        temp2 = np.where(categorized['MainIngredient3'] != i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 13

    ####Main3 Comparing####
    ##Main3 & 3 Comparing

    for i in input_main3:
        temp = np.where(categorized['MainIngredient3'] == i)
        temp2 = np.where(categorized['MainIngredient3'] != i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 13

    ##Main3 & 1 comparing
    for i in input_main3:
        temp = np.where(categorized['MainIngredient1'] == i)
        temp2 = np.where(categorized['MainIngredient1'] != i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 8

    ##Main3 & 2 comparing
    for i in input_main3:
        temp = np.where(categorized['MainIngredient2'] == i)
        temp2 = np.where(categorized['MainIngredient2'] != i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 13

    ##Sub1
    for i in input_sub1:
        temp = np.where(categorized['SubIngredient1'] == i)
        temp2 = np.where(categorized['SubIngredient1'] != i)

    if len(temp) > 0:
        categorized['score'][temp[0]] += 12

    print(categorized)

    ##Drop item if score is under 45
    for i in categorized['score']:
        temp = np.where(categorized['score']<40)

    if len(temp) > 0:
        categorized = categorized.drop(temp[0], 0)
        categorized = categorized.reset_index(drop=True)


    ##Sorting  by score
    categorized = categorized.sort_values(["score"], ascending=[False])
    categorized = categorized.reset_index(drop=True)

    if len(categorized) >=3:
        for i in categorized['score']:
            temp = np.where(categorized['score'] < categorized['score'][2])
        if len(temp) > 0:
            categorized = categorized.drop(temp[0], 0)
            categorized = categorized.reset_index(drop=True)








    print('sort')
    print(categorized)
    print('')
    print(input)
  





    length =len(categorized['Name'])
    result= []


    for  i in range(0,length):

        dict_val = {
                  "ImageURL":categorized['ImageURL'][i],
                  "Category":categorized['Category'][i],
                  "Category2":categorized['Cookery'][i],
                  "Name":categorized['Name'][i],
                  "Main Ingredient1":categorized['MainIngredient1'][i],
                  "Main Ingredient2":categorized['MainIngredient2'][i],
                  "Main Ingredient3":categorized['MainIngredient3'][i],
                  "Sub Ingredient1":categorized['SubIngredient1'][i],
                  "detail":categorized['detail'][i]
                  }
        
        result.append(dict_val)





    return {
        "statusCode": 200,


        "length": length,
        "Food" : result
    



    }
