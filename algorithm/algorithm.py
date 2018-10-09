# -*- coding: utf-8 -*-

import pandas as pd
import numpy as np

from typing import Any, Union, List, Iterable

input = pd.read_excel('input.xlsx')
output = pd.read_excel('output.xlsx')

#add another column 'score'
output['score'] = 0

input_name = input['name']
input_category = input['category']
input_main1 = input['main1']
input_main2 = input['main2']
input_main3 = input['main3']
input_sub1 = input['sub1']
output_name = output['name']
output_category = output['category']
output_main1 = output['main1']
output_main2 = output['main2']
output_main3 = output['main3']
output_sub1 = output['sub1']
temp = []
temp2 = []  # type: List[Any]

print(input)
print(output)

##Categorizing
for i in input_category:
    for j in output_category:
        if i == j:
            temp = np.where(output_category != j)

categorized = output.drop(temp[0],0)
categorized = categorized.reset_index(drop=True)


print(categorized)

temp = []

##Main1
for i in input_main1:
    for j in categorized['main1']:
        if i == j:
            temp = np.where(categorized['main1'] == i)
        else:
            temp2 = np.where(categorized['main1'] != i)


categorized['score'][temp[0]] += 30
categorized['score'][temp2[0]] += 5


print(categorized)

temp = []
temp2 = []

##Main2
for i in input_main2:
    for j in categorized['main2']:
        if i == j:
            temp = np.where(categorized['main2'] == i)
        else:
            temp2 = np.where(categorized['main2'] != i)

print(temp)
print(temp2)


categorized['score'][temp[0]] += 20
categorized['score'][temp2[0]] += 5

print('MAIN2222')
print(input)
print(categorized)


temp = []
temp2 = []

##Main2 & 3 comparing
for i in input_main2:
    for j in categorized['main3']:
        if i == j:
            temp = np.where(categorized['main3'] == i)
        else:
            temp2 = np.where(categorized['main3'] != i)

if temp or temp2 == True:
    categorized['score'][temp[0]] += 7
    categorized['score'][temp2[0]] += 0


print(temp)
print(temp2)
print('MAIN23')
print(input)
print(categorized)

temp = []
temp2 = []


##Main3
for i in input_main3:
    for j in categorized['main3']:
        if i == j:
            temp = np.where(categorized['main3'] == i)
        else:
            temp2 = np.where(categorized['main3'] != i)


if temp or temp2 == True:
    categorized['score'][temp[0]] += 10
    categorized['score'][temp2[0]] += 5

print('MAIN333')
print(input)
print(categorized)


temp = []
temp2 = []

##Main3 & 2 comparing
for i in input_main3:
    for j in categorized['main2']:
        if i == j:
            temp = np.where(categorized['main2'] == i)

if temp or temp2 == True:
    categorized['score'][temp[0]] += 4
    categorized['score'][temp2[0]] += 0

temp = []
temp2 = []

##Sub1
for i in input_sub1:
    for j in categorized['sub1']:
        if i == j:
            temp = np.where(categorized['sub1'] == i)
        else:
            temp2 = np.where(categorized['sub1'] != i)


categorized['score'][temp[0]] += 10
categorized['score'][temp2[0]] += 0


print('SUB1')
print(input)
print(categorized)


categorized = categorized.sort_values(["score"], ascending = [False])

print('sort')
print(categorized)
print('')
print('########RESULT########')
print(categorized['name'])










