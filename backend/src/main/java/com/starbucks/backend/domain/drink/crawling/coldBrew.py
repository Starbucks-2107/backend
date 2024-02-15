from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from bs4 import BeautifulSoup

driver = webdriver.Chrome()

url = 'https://www.starbucks.co.kr/menu/drink_list.do?CATE_CD=product_brewed'
driver.get(url)

driver.implicitly_wait(10)
html = driver.page_source
soup = BeautifulSoup(html, 'html.parser')

product_cold_brew = soup.find('ul', class_='product_cold_brew')
if product_cold_brew:
    cold_brew_items = product_cold_brew.find_all('li', class_='menuDataSet')

    for item in cold_brew_items:
        name = item.find('dd').text.strip()
        image_url = item.find('img')['src'].strip()
        print(f'Name: {name}, Image URL: {image_url}')
else:
    print("Cold brew products not found.")

driver.quit()
