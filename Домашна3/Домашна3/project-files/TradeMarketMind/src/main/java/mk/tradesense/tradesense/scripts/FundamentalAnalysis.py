import pandas as pd
from sqlalchemy import create_engine
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer
from dotenv import load_dotenv
import re
import nltk
import os

nltk.download('punkt_tab')

load_dotenv()

db_details = {
    "dbname": os.getenv("DB_NAME"),
    "user": os.getenv("DB_USER"),
    "password": os.getenv("DB_PASSWORD"),
    "host": os.getenv("DB_HOST"),
    "port": os.getenv("DB_PORT")
}

print("DB_NAME:", os.getenv("DB_NAME"))
print("DB_USER:", os.getenv("DB_USER"))
print("DB_PASSWORD:", os.getenv("DB_PASSWORD"))
print("DB_HOST:", os.getenv("DB_HOST"))
print("DB_PORT:", os.getenv("DB_PORT"))

engine = create_engine(
    f'postgresql://{db_details["user"]}:{db_details["password"]}@{db_details["host"]}:{db_details["port"]}/{db_details["dbname"]}'
)

analyzer = SentimentIntensityAnalyzer()


def clean_issuer(issuer):
    cleaned_issuer = re.sub(r'[^a-zA-Z0-9]', '', issuer).upper()
    return cleaned_issuer


def analyze_and_save_sentiment():
    query = 'SELECT "issuer", "symbol", "news_content" FROM issuer_news'
    df = pd.read_sql(query, engine)

    if df.empty:
        print("No data found in issuer_news table.")
        return

    df = df.dropna(subset=["issuer", "symbol", "news_content"])
    sentiment_data = []
    df['cleaned_issuer'] = df['issuer'].apply(clean_issuer)

    for _, row in df.iterrows():
        issuer = row['cleaned_issuer']
        symbol = row['symbol']
        news_content = row['news_content']

        sentences = nltk.sent_tokenize(news_content)

        relevant_sentences = [sentence for sentence in sentences if issuer in sentence.replace(' ', '').upper()]

        if relevant_sentences:
            aggregated_text = " ".join(relevant_sentences)
            sentiment_score = analyzer.polarity_scores(aggregated_text)['compound']
            sentiment = 'positive' if sentiment_score > 0.05 else 'negative' if sentiment_score < -0.05 else 'neutral'
            sentiment_data.append({'symbol': symbol, 'sentiment': sentiment})

    if sentiment_data:
        sentiment_df = pd.DataFrame(sentiment_data)
        existing_data = pd.read_sql('SELECT "symbol" FROM sentiments', engine)
        existing_symbols = set(existing_data['symbol'])
        sentiment_df = sentiment_df[~sentiment_df['symbol'].isin(existing_symbols)]

        if not sentiment_df.empty:
            sentiment_df.to_sql('sentiments', engine, if_exists='replace', index=False)
            print(f"Sentiment analysis results for symbols have been saved to the 'sentiments' table.")
        else:
            print("No new sentiment data to save.")
    else:
        print("No relevant sentences found for sentiment analysis.")


analyze_and_save_sentiment()