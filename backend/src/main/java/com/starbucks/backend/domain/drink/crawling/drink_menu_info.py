from selenium import webdriver
from bs4 import BeautifulSoup
import csv
import re

# WebDriver 설정
options = webdriver.ChromeOptions()
options.add_argument("--headless")  # 브라우저를 띄우지 않는 headless 모드
driver = webdriver.Chrome(options=options)

detail_base_url = 'https://www.starbucks.co.kr/menu/drink_view.do?product_cd='

input_csv_file = '제품 이미지 정보.csv'
output_csv_file = '제품 영양 정보.csv'

drink_info_data = []
drink_description_data = []

# CSV 파일 읽기
with open(input_csv_file, 'r', encoding='utf-8') as csvfile:
    reader = csv.reader(csvfile)
    next(reader)
    for row in reader:
        image_url = row[0]
        match = re.search(r'\[(\d+)\]', image_url)
        if match:
            product_code = match.group(1)
            detail_url = detail_base_url + product_code

            driver.get(detail_url)
            driver.implicitly_wait(5)

            soup = BeautifulSoup(driver.page_source, 'html.parser')

            # 제품 이름, 한마디
            my_assign_zone = soup.find('div', class_='myAssignZone')
            if my_assign_zone:
                h4_tag = my_assign_zone.find('h4')
                drink_description = my_assign_zone.find('p', class_='t1')
                if h4_tag and h4_tag.contents:
                    name_ko = h4_tag.contents[0].strip() if h4_tag.contents[0] else ''
                    span_tag = h4_tag.find('span')
                    name_eng = span_tag.text.strip() if span_tag else ''

                if drink_description and drink_description.contents:
                    description = drink_description.contents[0].strip() if drink_description.contents[0] else ''
            else:
                name_ko = ''
                name_eng = ''

            # 영양 정보 추출
            nutrition_info = soup.select_one('div.product_info_content')
            kcal = (re.findall(r'\d+', nutrition_info.select_one('li.kcal').text.strip()) + ['0'])[0] if nutrition_info.select_one('li.kcal') else '0'
            sat_FAT = (re.findall(r'\d+', nutrition_info.select_one('li.sat_FAT').text.strip()) + ['0'])[0] if nutrition_info.select_one('li.sat_FAT') else '0'
            protein = (re.findall(r'\d+', nutrition_info.select_one('li.protein').text.strip()) + ['0'])[0] if nutrition_info.select_one('li.protein') else '0'
            sodium = (re.findall(r'\d+', nutrition_info.select_one('li.sodium').text.strip()) + ['0'])[0] if nutrition_info.select_one('li.sodium') else '0'
            sugars = (re.findall(r'\d+', nutrition_info.select_one('li.sugars').text.strip()) + ['0'])[0] if nutrition_info.select_one('li.sugars') else '0'
            caffeine = (re.findall(r'\d+', nutrition_info.select_one('li.caffeine').text.strip()) + ['0'])[0] if nutrition_info.select_one('li.caffeine') else '0'

            # 알러지 유발 요인
            allergy_info = soup.select('div.product_factor p')[0].text.strip()

            # 크기 및 내용량 추출
            size_info = soup.select_one('div.product_view_info div.product_info_head').text.strip()
            size_match = re.search(r'([A-Za-z]+)\(([^\)]+)\) \/ (\d+)ml \((\d+ fl oz)\)', size_info)
            if size_match:
                size = size_match.group(1)  # 'Tall'
                size_ko = size_match.group(2)  # '톨'
                ml = size_match.group(3)  # '355'
                oz = size_match.group(4)  # '12'
            else:
                size = size_ko = ml = oz = ''

            # CSV 파일에 데이터 쓰기
            drink_info_data.append([name_ko, name_eng, size, size_ko, ml, oz, kcal, sat_FAT, protein, sodium, sugars, caffeine])
            drink_description_data.append([name_ko, description, allergy_info])

driver.quit()


menu_info_file_path = '제품 영양 정보.csv'
with open(menu_info_file_path, 'w', newline='', encoding='utf-8') as file:
    writer = csv.writer(file)
    writer.writerow(['Name_ko', 'Name_eng', 'Size_eng', 'Size_ko', 'ml', 'oz', '1회 제공량 (kcal)', '포화지방 (g)', '단백질 (g)', '나트륨 (mg)', '당류 (g)', '카페인 (mg)', '알레르기 유발요인'])
    writer.writerows(drink_info_data)

drink_description = '음료 설명.csv'
with open(drink_description, 'w', newline='', encoding='utf-8') as file:
    writer = csv.writer(file)
    writer.writerow(['Name_ko', '음료 설명', '알러지 요인'])
    writer.writerows(drink_description_data)


print(f'CSV file has been saved to {menu_info_file_path}')
print(f'CSV file has been saved to {drink_description}')
