import pandas as pd
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
from sqlalchemy import create_engine
import os

driver = webdriver.Chrome()

base_url = "https://www.mse.mk/en/issuer/"
issuers = [
    'komercijalna-banka-ad-skopje/', 'alkaloid-ad-skopje/', 'vv-tikves-ad-skopje/', 'vitaminka-ad-prilep/',
    'granit-ad-skopje/', 'ds-smith-ad-skopje/', 'zito-luks-ad-skopje/', 'zk-pelagonija-ad-bitola/',
    'internesnel-hotels-ad-skopje/', 'makedonijaturist-ad-skopje/', 'makoteks-ad-skopje/', 'makosped-ad-skopje/',
    'makpetrol-ad-skopje/', 'makstil-ad-skopje/', 'replek-ad-skopje/', 'rz-inter-transsped-ad-skopje/',
    'rz-uslugi-ad-skopje/', 'skopski-pazar-ad-skopje/', 'stopanska-banka-ad-bitola/', 'teteks-ad-tetovo/',
    'ttk-banka-ad-skopje/', 'tutunski-kombinat-ad-prilep/', 'fersped-ad-skopje/', 'hoteli-metropol-ohrid/',
    'agromehanika-ad-skopje/', 'ading-ad--skopje/', 'angropromet-tikvesanka-ad-kavadarci/',
    'arcelormittal-(hrm)-ad-skopje/', 'automakedonija-ad-skopje/', 'bim-ad-sveti-nikole/', 'blagoj-tufanov-ad-radovis/',
    'vabtek-mzt-ad-skopje/', 'veteks-ad-veles/', 'gd-tikves-ad-kavadarci/', 'geras-cunev-konfekcija-ad-strumica/',
    'geras-cunev-trgovija-ad-strumica/', 'grozd-ad-strumica/', 'gtc-ad-skopje/', 'debarski-bani-–capa-ad--debar/',
    'dimko-mitrev-veles/', 'evropa-ad-skopje/', 'zas-ad-skopje/', 'zito-karaorman-ad-kicevo/',
    'zito-polog-ad-tetovo/', 'interpromet-ad-tetovo/', 'klanica-so-ladilnik-ad-strumica/', 'blagoj-gorev-ad-veles/',
    'liberti-ad-skopje/', 'lotarija-na-makedonija-ad-skopje/',
    'osiguruvane-makedonija-ad-skopje---viena-insurens-grup/',
    'makedonski-telekom-ad-–-skopje/', 'makpromet-ad-stip/', 'mermeren-kombinat-ad-prilep/', 'mzt-pumpi-ad-skopje/',
    'moda-ad-sveti-nikole/', 'nematali-ograzden-ad-strumica/', 'nlb-banka-ad-skopje/',
    'nova-stokovna-kuka-ad-strumica/',
    'oilko-kda-skopje/', 'okta-ad-skopje/', 'oranzerii-hamzali-strumica/', 'patnicki-soobrakaj-transkop-ad-bitola/',
    'pekabesko-ad-kadino-ilinden/', 'pelisterka-ad-skopje/', 'popova-kula-ad-demir-kapija/',
    'prilepska-pivarnica-ad-prilep/',
    'rade-koncar--aparatna-tehnika-ad-skopje/', 'rz-ekonomika-ad-skopje/', 'rz-tehnicka-kontrola-ad-skopje/',
    'rudnici-banani-ad-skopje/', 'sigurnosno-staklo-ad-prilep/', 'sileks-ad-kratovo/', 'slavej--ad-skopje/',
    'sovremen-dom-ad-prilep/', 'stokopromet-ad-skopje/', 'stopanska-banka-ad-skopje/', 'strumicko-pole-s-vasilevo/',
    'tajmiste-ad-kicevo/', 'teal-ad--tetovo/', 'tehnokomerc-ad-skopje/', 'trgotekstil-maloprodazba-ad-skopje/',
    'triglav-osiguruvane-ad-skopje/', 'trudbenik-ad-ohrid/', 'ugotur-ad-skopje/', 'unibanka-ad-skopje/',
    'fabrika-karpos-ad-skopje/', 'fakom-ad-skopje/', 'fzc-11-ti-oktomvri-ad-kumanovo/', 'fruktal-mak-ad-skopje/',
    'fustelarko-borec-ad-bitola/', 'cementarnica-usje-ad-skopje/', 'centralna-kooperativna-banka-ad-skopje/'
]

db_details = {
    "dbname": os.getenv("DB_NAME"),
    "user": os.getenv("DB_USER"),
    "password": os.getenv("DB_PASSWORD"),
    "host": os.getenv("DB_HOST"),
    "port": os.getenv("DB_PORT")
}

df = pd.DataFrame(columns=["issuer", "symbol", "news_content"])


def get_issuer_data(issuer_url):
    full_url = base_url + issuer_url
    driver.get(full_url)

    try:
        symbol_element = WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, "#symbols>li>a"))
        )
        symbol = symbol_element.text
    except Exception:
        symbol = None

    try:
        second_tab_element = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.CSS_SELECTOR, "#myTab5>li>a"))
        )
        actions = ActionChains(driver)
        actions.move_to_element(second_tab_element).click().perform()
    except Exception:
        pass

    try:
        news_elements = WebDriverWait(driver, 10).until(
            EC.presence_of_all_elements_located((By.CSS_SELECTOR, "#mCSB_3_container > ol > li > div > div > div > a"))
        )
        news_list = [(news.get_attribute("href")) for news in news_elements]
    except Exception:
        news_list = []

    all_news_content = []
    for news_link in news_list:
        driver.get(news_link)
        time.sleep(2)

        try:
            paragraphs = WebDriverWait(driver, 10).until(
                EC.presence_of_all_elements_located((By.CSS_SELECTOR, "#content p"))
            )
            news_content = "\n".join([para.text for para in paragraphs])
            all_news_content.append(news_content)
        except Exception:
            pass

    all_news_content_text = "\n\n".join(all_news_content)
    df.loc[len(df)] = [issuer_url, symbol, all_news_content_text]


for issuer_url in issuers:
    get_issuer_data(issuer_url)
    time.sleep(2)

driver.quit()

df.rename(columns={'news_content': 'news_content'}, inplace=True)
engine = create_engine(
    f'postgresql://{db_details["user"]}:{db_details["password"]}@{db_details["host"]}:{db_details["port"]}/{db_details["dbname"]}')
df.to_sql('issuer_news', engine, if_exists='replace', index=False)
