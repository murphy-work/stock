CREATE TABLE IF NOT EXISTS fund
(
    code                      VARCHAR(6)     NOT NULL PRIMARY KEY,
    name                      VARCHAR(20)    NOT NULL,
    exchange                  VARCHAR(2)     NULL,
    type                      VARCHAR(10)    NOT NULL,
    quotation_daily_sync_time TIMESTAMP      NULL,
    opening_price             NUMERIC(16, 3) NULL,
    highest_price             NUMERIC(16, 3) NULL,
    lowest_price              NUMERIC(16, 3) NULL,
    closing_price             NUMERIC(16, 3) NULL,
    trading_volume            INTEGER        NULL,
    amplitude                 NUMERIC(16, 3) NULL,
    percent_change            NUMERIC(16, 2) NULL,
    price_change              NUMERIC(16, 2) NULL
);
COMMENT ON TABLE fund IS '基金';
COMMENT ON COLUMN fund.code IS '代码';
COMMENT ON COLUMN fund.name IS '名称';
COMMENT ON COLUMN fund.exchange IS '交易所';
COMMENT ON COLUMN fund.type IS '类型';
COMMENT ON COLUMN fund.quotation_daily_sync_time IS '每日行情同步时间';
COMMENT ON COLUMN fund.opening_price IS '开盘价（元）';
COMMENT ON COLUMN fund.highest_price IS '最高价（元）';
COMMENT ON COLUMN fund.lowest_price IS '最低价（元）';
COMMENT ON COLUMN fund.closing_price IS '收盘价（元）';
COMMENT ON COLUMN fund.trading_volume IS '成交量（手）';
COMMENT ON COLUMN fund.amplitude IS '振幅（%）';
COMMENT ON COLUMN fund.percent_change IS '涨跌幅（%）';
COMMENT ON COLUMN fund.price_change IS '涨跌额（元）';

CREATE TABLE IF NOT EXISTS fund_quotation_daily
(

    id             VARCHAR(14)    NOT NULL PRIMARY KEY,
    code           VARCHAR(6)     NULL,
    trade_date     DATE           NULL,
    opening_price  NUMERIC(16, 3) NULL,
    highest_price  NUMERIC(16, 3) NULL,
    lowest_price   NUMERIC(16, 3) NULL,
    closing_price  NUMERIC(16, 3) NULL,
    trading_volume INTEGER        NULL,
    amplitude      NUMERIC(16, 3) NULL,
    percent_change NUMERIC(16, 2) NULL,
    price_change   NUMERIC(16, 2) NULL
);
COMMENT ON TABLE fund_quotation_daily IS '基金每日行情';
COMMENT ON COLUMN fund_quotation_daily.id IS '编号 基金代码+交易时间 15900120240229';
COMMENT ON COLUMN fund_quotation_daily.code IS '基金代码';
COMMENT ON COLUMN fund_quotation_daily.trade_date IS '交易时间';
COMMENT ON COLUMN fund_quotation_daily.opening_price IS '开盘价（元）';
COMMENT ON COLUMN fund_quotation_daily.highest_price IS '最高价（元）';
COMMENT ON COLUMN fund_quotation_daily.lowest_price IS '最低价（元）';
COMMENT ON COLUMN fund_quotation_daily.closing_price IS '收盘价（元）';
COMMENT ON COLUMN fund_quotation_daily.trading_volume IS '成交量（股）';
COMMENT ON COLUMN fund_quotation_daily.amplitude IS '振幅（%）';
COMMENT ON COLUMN fund_quotation_daily.percent_change IS '涨跌幅（%）';
COMMENT ON COLUMN fund_quotation_daily.price_change IS '涨跌额（元）';
CREATE INDEX IF NOT EXISTS fund_quotation_daily_code_index ON fund_quotation_daily (code);
CREATE INDEX IF NOT EXISTS fund_quotation_daily_trade_date_index ON fund_quotation_daily (trade_date);

CREATE TABLE IF NOT EXISTS industry
(
    code        VARCHAR(3)  NOT NULL PRIMARY KEY,
    name        VARCHAR(30) NOT NULL,
    code_parent VARCHAR(1)  NOT NULL,
    name_parent VARCHAR(30) NOT NULL
);
COMMENT ON TABLE industry IS '证监会二级行业';
COMMENT ON COLUMN industry.code IS '二级行业代码';
COMMENT ON COLUMN industry.name IS '二级行业名称';
COMMENT ON COLUMN industry.code_parent IS '所属一级行业代码';
COMMENT ON COLUMN industry.name_parent IS '所属一级行业名称';

CREATE TABLE IF NOT EXISTS stock
(
    code                      VARCHAR(6)  NOT NULL PRIMARY KEY,
    name                      VARCHAR(20) NOT NULL,
    exchange                  VARCHAR(2)  NOT NULL,
    industry_code             VARCHAR(3)  NULL,
    industry_name             VARCHAR(30) NULL,
    quotation_daily_sync_time TIMESTAMP   NULL
);
COMMENT ON TABLE stock IS '股票';
COMMENT ON COLUMN stock.code IS '代码';
COMMENT ON COLUMN stock.name IS '名称';
COMMENT ON COLUMN stock.exchange IS '交易所';
COMMENT ON COLUMN stock.industry_code IS '证监会二级行业代码';
COMMENT ON COLUMN stock.industry_name IS '证监会二级行业名称';
COMMENT ON COLUMN stock.quotation_daily_sync_time IS '每日行情同步时间';
CREATE INDEX IF NOT EXISTS stock_industry_code_index ON stock (industry_code);

CREATE TABLE IF NOT EXISTS stock_quotation_daily
(
    id                 VARCHAR(14)    NOT NULL PRIMARY KEY,
    code               VARCHAR(6)     NULL,
    trade_date         DATE           NULL,
    opening_price      NUMERIC(16, 2) NULL,
    highest_price      NUMERIC(16, 2) NULL,
    lowest_price       NUMERIC(16, 2) NULL,
    closing_price      NUMERIC(16, 2) NULL,
    trading_volume     INTEGER        NULL,
    transaction_amount NUMERIC(16, 2) NULL,
    amplitude          NUMERIC(16, 2) NULL,
    turnover_rate      NUMERIC(16, 2) NULL,
    percent_change     NUMERIC(16, 2) NULL,
    price_change       NUMERIC(16, 2) NULL
);
COMMENT ON TABLE stock_quotation_daily IS '股票每日行情';
COMMENT ON COLUMN stock_quotation_daily.id IS '编号 股票代码+交易时间 00000120240229';
COMMENT ON COLUMN stock_quotation_daily.code IS '股票代码';
COMMENT ON COLUMN stock_quotation_daily.trade_date IS '交易时间';
COMMENT ON COLUMN stock_quotation_daily.opening_price IS '开盘价（元）';
COMMENT ON COLUMN stock_quotation_daily.highest_price IS '最高价（元）';
COMMENT ON COLUMN stock_quotation_daily.lowest_price IS '最低价（元）';
COMMENT ON COLUMN stock_quotation_daily.closing_price IS '收盘价（元）';
COMMENT ON COLUMN stock_quotation_daily.trading_volume IS '成交量（手）';
COMMENT ON COLUMN stock_quotation_daily.transaction_amount IS '成交额（元）';
COMMENT ON COLUMN stock_quotation_daily.amplitude IS '振幅（%）';
COMMENT ON COLUMN stock_quotation_daily.turnover_rate IS '换手率（%）';
COMMENT ON COLUMN stock_quotation_daily.percent_change IS '涨跌幅（%）';
COMMENT ON COLUMN stock_quotation_daily.price_change IS '涨跌额（元）';
CREATE INDEX IF NOT EXISTS stock_quotation_daily_code_index ON stock_quotation_daily (code);
CREATE INDEX IF NOT EXISTS stock_quotation_daily_trade_date_index ON stock_quotation_daily (trade_date);

CREATE TABLE IF NOT EXISTS index
(
    code                      VARCHAR(8)  NOT NULL PRIMARY KEY,
    name                      VARCHAR(10) NOT NULL,
    exchange                  VARCHAR(4)  NOT NULL,
    quotation_daily_sync_time TIMESTAMP   NULL
);
COMMENT ON TABLE index IS '股票指数';
COMMENT ON COLUMN index.code IS '代码';
COMMENT ON COLUMN index.name IS '名称';
COMMENT ON COLUMN index.exchange IS '交易所';
COMMENT ON COLUMN index.quotation_daily_sync_time IS '每日行情同步时间';

create table index_quotation_daily
(
    id                 VARCHAR(14)    NOT NULL PRIMARY KEY,
    code               VARCHAR(6)     NULL,
    trade_date         DATE           NULL,
    opening_price      NUMERIC(16, 2) NULL,
    highest_price      NUMERIC(16, 2) NULL,
    lowest_price       NUMERIC(16, 2) NULL,
    closing_price      NUMERIC(16, 2) NULL,
    trading_volume     INTEGER        NULL,
    transaction_amount NUMERIC(16, 2) NULL,
    amplitude          NUMERIC(16, 2) NULL,
    turnover_rate      NUMERIC(16, 2) NULL,
    percent_change     NUMERIC(16, 2) NULL,
    price_change       NUMERIC(16, 2) NULL
);
COMMENT ON TABLE index_quotation_daily IS '指数每日行情';
COMMENT ON COLUMN index_quotation_daily.id IS '编号 指数代码+交易时间 00000120240229';
COMMENT ON COLUMN index_quotation_daily.code IS '指数代码';
COMMENT ON COLUMN index_quotation_daily.trade_date IS '交易时间';
COMMENT ON COLUMN index_quotation_daily.opening_price IS '开盘价（元）';
COMMENT ON COLUMN index_quotation_daily.highest_price IS '最高价（元）';
COMMENT ON COLUMN index_quotation_daily.lowest_price IS '最低价（元）';
COMMENT ON COLUMN index_quotation_daily.closing_price IS '收盘价（元）';
COMMENT ON COLUMN index_quotation_daily.trading_volume IS '成交量（手）';
COMMENT ON COLUMN index_quotation_daily.transaction_amount IS '成交额（元）';
COMMENT ON COLUMN index_quotation_daily.amplitude IS '振幅（%）';
COMMENT ON COLUMN index_quotation_daily.turnover_rate IS '换手率（%）';
COMMENT ON COLUMN index_quotation_daily.percent_change IS '涨跌幅（%）';
COMMENT ON COLUMN index_quotation_daily.price_change IS '涨跌额（元）';
CREATE INDEX IF NOT EXISTS index_quotation_daily_code_index ON index_quotation_daily (code);
CREATE INDEX IF NOT EXISTS index_quotation_daily_trade_date_index ON index_quotation_daily (trade_date);

