# -*- coding: utf-8 -*-

import pandas as pd
import numpy as np
import time
from typing import Any, Union, List, Iterable

##time measuring
start = time.time()

input = pd.read_excel('input.xlsx')
output = pd.read_excel('output2.xlsx')

# add another column 'score'
output['score'] = 0

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

##Create Categorized Dataframe
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
    categorized['score'][temp[0]] += 30
if len(temp2) > 0:
    categorized['score'][temp2[0]] += 5

##Main1 & 2 comparing
for i in input_main1:
    temp = np.where(categorized['MainIngredient2'] == i)
    temp2 = np.where(categorized['MainIngredient2'] != i)

if len(temp) > 0:
    categorized['score'][temp[0]] += 15

##Main1 & 3 comparing
for i in input_main1:
    temp = np.where(categorized['MainIngredient3'] == i)
    temp2 = np.where(categorized['MainIngredient3'] != i)

if len(temp) > 0:
    categorized['score'][temp[0]] += 10


####Main2 Comparing###
##Main2 & 2 comparing

for i in input_main2:
    temp = np.where(categorized['MainIngredient2'] == i)
    temp2 = np.where(categorized['MainIngredient2'] != i)

if len(temp) > 0:
    categorized['score'][temp[0]] += 20
if len(temp2) > 0:
    categorized['score'][temp2[0]] += 5

##Main2 & 1 comparing
for i in input_main2:
    temp = np.where(categorized['MainIngredient1'] == i)
    temp2 = np.where(categorized['MainIngredient1'] != i)

if len(temp) > 0:
    categorized['score'][temp[0]] += 13

##Main2 & 3 comparing
for i in input_main2:
    temp = np.where(categorized['MainIngredient3'] == i)
    temp2 = np.where(categorized['MainIngredient3'] != i)

if len(temp) > 0:
    categorized['score'][temp[0]] += 10

####Main3 Comparing####
##Main3 & 3 Comparing

for i in input_main3:
    temp = np.where(categorized['MainIngredient3'] == i)
    temp2 = np.where(categorized['MainIngredient3'] != i)

if len(temp) > 0:
    categorized['score'][temp[0]] += 15

##Main3 & 1 comparing
for i in input_main3:
    temp = np.where(categorized['MainIngredient1'] == i)
    temp2 = np.where(categorized['MainIngredient1'] != i)

if len(temp) > 0:
    categorized['score'][temp[0]] += 4

##Main3 & 2 comparing
for i in input_main3:
    temp = np.where(categorized['MainIngredient2'] == i)
    temp2 = np.where(categorized['MainIngredient2'] != i)

if len(temp) > 0:
    categorized['score'][temp[0]] += 8

##Sub1
for i in input_sub1:
    temp = np.where(categorized['SubIngredient1'] == i)
    temp2 = np.where(categorized['SubIngredient1'] != i)

if len(temp) > 0:
    categorized['score'][temp[0]] += 15

print(categorized)


##Drop item if score is under 36
for i in categorized['score']:
    temp = np.where(categorized['score'] <= 36)

if len(temp) > 0:
    categorized = categorized.drop(temp[0], 0)
    categorized = categorized.reset_index(drop=True)

##Sorting  by score
categorized = categorized.sort_values(["score"], ascending=[False])
categorized = categorized.reset_index(drop=True)


print('sort')
print(categorized)
print('')
print('########RESULT########')
print(categorized['Name'])
end = time.time()

elapsed = end - start
print('')
print('')
print('TIME MEASUREMENT')
print(elapsed)
