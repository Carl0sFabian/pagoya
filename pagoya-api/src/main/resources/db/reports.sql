-- Ejecutar manualmente despues del primer arranque (cuando Hibernate ya
-- creo las tablas). Ambos archivos se cargan con psql:
--   psql -h localhost -p 55432 -U postgres -d pagoya_db -f src/main/resources/data.sql
--   psql -h localhost -p 55432 -U postgres -d pagoya_db -f src/main/resources/db/reports.sql

-- 1. Total transferido por moneda (solo COMPLETED)
CREATE OR REPLACE FUNCTION fn_transfer_report_by_currency()
RETURNS TABLE(currency VARCHAR, total_transfers BIGINT, total_amount NUMERIC) AS $$
BEGIN
    RETURN QUERY
SELECT t.currency::VARCHAR, COUNT(*)::BIGINT, SUM(t.amount)::NUMERIC
FROM transfers t
WHERE t.status = 'COMPLETED'
GROUP BY t.currency
ORDER BY SUM(t.amount) DESC;
END;
$$ LANGUAGE plpgsql;

-- 2. Transferencias por dia en rango
CREATE OR REPLACE FUNCTION fn_transfer_report_by_day(p_from DATE, p_to DATE)
RETURNS TABLE(day DATE, total_transfers BIGINT, total_amount NUMERIC) AS $$
BEGIN
    RETURN QUERY
SELECT DATE(t.created_at), COUNT(*)::BIGINT, SUM(t.amount)::NUMERIC
FROM transfers t
WHERE DATE(t.created_at) BETWEEN p_from AND p_to
GROUP BY DATE(t.created_at)
ORDER BY DATE(t.created_at);
END;
$$ LANGUAGE plpgsql;

-- 3. Distribucion por estado
CREATE OR REPLACE FUNCTION fn_transfer_report_by_status()
RETURNS TABLE(status VARCHAR, total BIGINT) AS $$
BEGIN
    RETURN QUERY
SELECT t.status::VARCHAR, COUNT(*)::BIGINT
FROM transfers t
GROUP BY t.status;
END;
$$ LANGUAGE plpgsql;

-- 4. Cuentas por tipo y estado
CREATE OR REPLACE FUNCTION fn_account_report_summary()
RETURNS TABLE(type VARCHAR, status VARCHAR, total BIGINT, total_balance NUMERIC) AS $$
BEGIN
    RETURN QUERY
SELECT a.type::VARCHAR, a.status::VARCHAR,
    COUNT(*)::BIGINT, COALESCE(SUM(a.balance), 0)::NUMERIC
FROM accounts a
GROUP BY a.type, a.status
ORDER BY a.type, a.status;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION sp_payments_by_category(p_customer_id BIGINT)
RETURNS TABLE(category VARCHAR, total_count BIGINT, total_amount NUMERIC) AS $$
BEGIN
    RETURN QUERY
SELECT sp.category::VARCHAR,
    COUNT(bp.id)::BIGINT,
    COALESCE(SUM(bp.amount), 0)::NUMERIC
FROM bill_payments bp
         JOIN service_providers sp ON sp.id = bp.provider_id
WHERE bp.customer_id = p_customer_id
GROUP BY sp.category
ORDER BY total_amount DESC;
END;
$$ LANGUAGE plpgsql;