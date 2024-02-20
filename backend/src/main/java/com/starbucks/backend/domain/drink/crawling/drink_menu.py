from selenium import webdriver
from bs4 import BeautifulSoup
import csv

# WebDriver 설정
options = webdriver.ChromeOptions()
options.add_argument("--headless")  # 브라우저를 띄우지 않는 headless 모드
driver = webdriver.Chrome(options=options)

url = 'https://www.starbucks.co.kr/menu/drink_list.do?CATE_CD=product_brewed'
driver.get(url)
driver.implicitly_wait(10)

html = driver.page_source
soup = BeautifulSoup(html, 'html.parser')

categories = {
    'product_cold_brew': ('콜드 브루', 'Cold Brew'),
    'product_brewed': ('브루드 커피', 'Brewed Coffee'),
    'product_espresso': ('에스프레소', 'Espresso'),
    'product_frappuccino': ('프라푸치노', 'Frappuccino'),
    'product_blended': ('블랜디드', 'Blended'),
    'product_refresher': ('리프레셔', 'Starbucks Refreshers'),
    'product_fizzio': ('피지오', 'Starbucks Fizzio'),
    'product_tea': ('티바나', 'Teavana'),
    'product_etc': ('기타', 'Others'),
    'product_juice': ('병음료', 'RTD'),
}

menu_data = []
image_data = []

for category, names in categories.items():
    category_products = soup.find('ul', class_=category)
    if category_products:
        products = category_products.find_all('li', class_='menuDataSet')
        for product in products:
            name = product.find('dd').text.strip()
            image_url = product.find('img')['src'].strip()
            menu_data.append([names[0], names[1], name, image_url])
            image_data.append(image_url)

driver.quit()


# CSV 파일 저장
menu_file_path = '스타벅스 음료 메뉴.csv'
with open(menu_file_path, 'w', newline='', encoding='utf-8') as file:
    writer = csv.writer(file)
    writer.writerow(['Type_ko', 'Type_eng', 'Name_ko', 'Name_eng' 'Image URL'])
    writer.writerows(menu_data)

img_url_file_path = '제품 이미지 정보.csv'
with open(img_url_file_path, 'w', newline='', encoding='utf-8') as file:
    writer = csv.writer(file)
    writer.writerow(['Image URL'])
    for img_url in image_data:
        writer.writerow([img_url])

print(f'CSV file has been saved to {menu_file_path}')
print(f'CSV file has been saved to {img_url_file_path}')

