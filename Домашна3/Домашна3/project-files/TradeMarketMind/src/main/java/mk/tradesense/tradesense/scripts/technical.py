import os
from dotenv import load_dotenv
import pandas as pd
import numpy as np
import ta
from sqlalchemy import create_engine
import sys

load_dotenv()

DB_NAME = os.getenv("DB_NAME")
DB_USER = os.getenv("DB_USER")
DB_PASSWORD = os.getenv("DB_PASSWORD")
DB_HOST = os.getenv("DB_HOST")
DB_PORT = os.getenv("DB_PORT")

engine = create_engine(f'postgresql://{DB_USER}:{DB_PASSWORD}@{DB_HOST}:{DB_PORT}/{DB_NAME}')

issuer_code = sys.argv[1]
query = f"""
SELECT stock_code, date, last_price, max_price, min_price, quantity, total_turnover
FROM stock_items
WHERE stock_code = '{issuer_code}'
"""


def convert_to_float(value):
    return float(value.replace('.', '').replace(',', '.'))


def prepare_data(data):
    data['last_price'] = data['last_price'].bfill()
    data['min_price'] = data['min_price'].fillna(data['last_price'])
    data['max_price'] = data['max_price'].fillna(data['last_price'])
    data['date'] = pd.to_datetime(data['date'], format='%d.%m.%Y')
    data = data.sort_values(by='date').reset_index(drop=True)

    data['last_price'] = data['last_price'].apply(convert_to_float)
    data['max_price'] = data['max_price'].apply(convert_to_float)
    data['min_price'] = data['min_price'].apply(convert_to_float)
    data['quantity'] = data['quantity'].apply(lambda x: int(x.replace('.', '')))
    data['total_turnover'] = data['total_turnover'].apply(lambda x: int(x.replace('.', '')))

    return data


def calculate_tema(data, window):
    ema1 = data.ewm(span=window, adjust=False).mean()
    ema2 = ema1.ewm(span=window, adjust=False).mean()
    ema3 = ema2.ewm(span=window, adjust=False).mean()
    return 3 * ema1 - 3 * ema2 + ema3


def calculate_hma(data, window):
    half_window = window // 2
    sqrt_window = int(np.sqrt(window))

    wma_half = ta.trend.WMAIndicator(close=data, window=half_window).wma()
    wma_full = ta.trend.WMAIndicator(close=data, window=window).wma()

    transformed_series = 2 * wma_half - wma_full
    return ta.trend.WMAIndicator(close=transformed_series, window=sqrt_window).wma()


def calculate_indicators(data, windows):
    for window in windows:
        # Oscillators
        data[f'RSI_{window}'] = ta.momentum.RSIIndicator(data['last_price'], window=window).rsi()
        data[f'Stochastic_{window}'] = ta.momentum.StochasticOscillator(
            high=data['max_price'], low=data['min_price'], close=data['last_price'], window=window).stoch()
        data[f'CCI_{window}'] = ta.trend.CCIIndicator(
            high=data['max_price'], low=data['min_price'], close=data['last_price'], window=window).cci()
        data[f'WilliamsR_{window}'] = ta.momentum.WilliamsRIndicator(
            high=data['max_price'], low=data['min_price'], close=data['last_price'], lbp=window).williams_r()
        data[f'UltimateOscillator_{window}'] = ta.momentum.UltimateOscillator(
            high=data['max_price'], low=data['min_price'], close=data['last_price']).ultimate_oscillator()

        # Moving Averages
        data[f'SMA_{window}'] = data['last_price'].rolling(window=window).mean()
        data[f'EMA_{window}'] = data['last_price'].ewm(span=window, adjust=False).mean()
        data[f'WMA_{window}'] = ta.trend.WMAIndicator(close=data['last_price'], window=window).wma()
        data[f'TEMA_{window}'] = calculate_tema(data['last_price'], window)
        data[f'HMA_{window}'] = calculate_hma(data['last_price'], window)

    return data


def generate_signals(data, windows, required_margin=1.2):
    signals = []

    for i, row in data.iterrows():
        buy_score = sell_score = 0

        for window in windows:
            if row[f'RSI_{window}'] < 30:
                buy_score += 2
            elif row[f'RSI_{window}'] > 70:
                sell_score += 2

            if row[f'Stochastic_{window}'] < 20:
                buy_score += 1.5
            elif row[f'Stochastic_{window}'] > 80:
                sell_score += 1.5

            if row[f'CCI_{window}'] < -100:
                buy_score += 1
            elif row[f'CCI_{window}'] > 100:
                sell_score += 1

        for ma_type in ['SMA', 'EMA', 'WMA', 'TEMA', 'HMA']:
            for window in windows:
                ma_value = row[f'{ma_type}_{window}']
                if row['last_price'] > ma_value:
                    buy_score += 1
                elif row['last_price'] < ma_value:
                    sell_score += 1

        if buy_score > sell_score * required_margin:
            signals.append('Buy')
        elif sell_score > buy_score * required_margin:
            signals.append('Sell')
        else:
            signals.append('Hold')

    data['signal'] = signals
    return data


def save_signals_to_db(data):
    signals_data = data[['stock_code', 'date', 'signal', 'last_price']]
    signals_data.to_sql('signals', engine, if_exists='replace', index=False)


def main():
    data = pd.read_sql(query, engine)
    data = prepare_data(data)

    windows = [5, 10, 20]

    data = calculate_indicators(data, windows)

    data = generate_signals(data, windows)

    data = data.bfill()
    data = data.ffill()

    save_signals_to_db(data)
    print("Signals have been saved to the database.")


if __name__ == '__main__':
    main()
