--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

-- Started on 2023-07-11 00:06:40

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3404 (class 1262 OID 34702)
-- Name: TaxiBooking; Type: DATABASE; Schema: -; Owner: admin
--

CREATE DATABASE "TaxiBooking" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Vietnamese_Vietnam.utf8';


ALTER DATABASE "TaxiBooking" OWNER TO admin;

\connect "TaxiBooking"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2 (class 3079 OID 34703)
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- TOC entry 3405 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


--
-- TOC entry 241 (class 1255 OID 34826)
-- Name: change_car(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.change_car() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    change_time VARCHAR(20);
BEGIN
    IF TG_OP = 'INSERT' THEN
        IF EXTRACT(DAY FROM CURRENT_DATE) < 15 THEN
            change_time := CONCAT('Tháng ', EXTRACT(MONTH FROM CURRENT_DATE), ' ', EXTRACT(YEAR FROM CURRENT_DATE));
            IF EXISTS(SELECT * FROM salary WHERE "date" = change_time) THEN
                UPDATE salary
                SET supplementary = (SELECT supplementary FROM salary WHERE "date" = delete_time) + 5000000
                WHERE "date" = delete_time;
            END IF;
        END IF;
    ELSIF TG_OP = 'DELETE' THEN
        IF EXTRACT(DAY FROM CURRENT_DATE) < 15 THEN
            change_time := CONCAT('Tháng ', EXTRACT(MONTH FROM CURRENT_DATE), ' ', EXTRACT(YEAR FROM CURRENT_DATE));
            IF EXISTS(SELECT * FROM salary WHERE "date" = change_time) THEN
                UPDATE salary
                SET supplementary = (SELECT supplementary FROM salary WHERE "date" = delete_time) - 5000000
                WHERE "date" = delete_time;
            END IF;
        END IF;
    END IF;

    RETURN NEW;
END;
$$;


ALTER FUNCTION public.change_car() OWNER TO postgres;

--
-- TOC entry 239 (class 1255 OID 34823)
-- Name: change_employee(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.change_employee() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    delete_time VARCHAR(20);
BEGIN
    IF TG_OP = 'INSERT' THEN
        IF EXTRACT(DAY FROM CURRENT_DATE) < 15 THEN
            delete_time := CONCAT('Tháng ', EXTRACT(MONTH FROM CURRENT_DATE), ' ', EXTRACT(YEAR FROM CURRENT_DATE));
            IF EXISTS(SELECT * FROM salary WHERE "date" = delete_time) THEN
                UPDATE salary
                SET salary = (SELECT salary FROM salary WHERE "date" = delete_time) + 15000000
                WHERE "date" = delete_time;
            END IF;
        END IF;
    ELSIF TG_OP = 'DELETE' THEN
        IF EXTRACT(DAY FROM CURRENT_DATE) < 15 THEN
            delete_time := CONCAT('Tháng ', EXTRACT(MONTH FROM CURRENT_DATE), ' ', EXTRACT(YEAR FROM CURRENT_DATE));
            IF EXISTS(SELECT * FROM salary WHERE "date" = delete_time) THEN
                UPDATE salary
                SET salary = (SELECT salary FROM salary WHERE "date" = delete_time) - 15000000
                WHERE "date" = delete_time;
            END IF;
        END IF;
    END IF;

    RETURN NEW;
END;
$$;


ALTER FUNCTION public.change_employee() OWNER TO postgres;

--
-- TOC entry 227 (class 1255 OID 34820)
-- Name: update_request(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.update_request() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
    DECLARE
        count_request_canceled INT;
        count_request_delete INT;
        new_ids TEXT[];
        max_existing_id TEXT;
        new_id_counter INT := 0;
        new_id TEXT;
        temp_id TEXT;
        counter INT := 1;
    BEGIN
        IF NEW.id = 'r9999' THEN
            count_request_canceled := (SELECT COUNT(*) FROM request WHERE status LIKE 'Đã hủy');
            count_request_delete := (SELECT COUNT(*)/2 FROM request);
            IF count_request_canceled >= count_request_delete THEN 
                DELETE FROM request
                WHERE id IN (
                    SELECT id
                    FROM (
                        SELECT id
                        FROM request
                        WHERE status LIKE 'Đã hủy'
                        ORDER BY id ASC
                        LIMIT count_request_delete
                    ) subquery
                );
            ELSE
                DELETE FROM request
                WHERE id IN (
                    SELECT id
                    FROM (
                        (
                            SELECT id
                            FROM request
                            WHERE status LIKE 'Đã hủy'
                            ORDER BY id ASC
                            LIMIT count_request_canceled
                        )
                        UNION
                        (
                            SELECT id
                            FROM request
                            WHERE status LIKE 'Hoàn thành'
                            ORDER BY id ASC
                            LIMIT (count_request_delete - count_request_canceled)
                        )
                    ) subquery
                );
            END IF;
            max_existing_id := (SELECT MAX(id) FROM request);
            IF max_existing_id IS NOT NULL THEN
                new_id_counter := CAST(SUBSTRING(max_existing_id FROM 2) AS INTEGER);
            END IF;
            FOR temp_id IN
                SELECT id
                FROM request
                WHERE id LIKE 'r%' AND id <> 'r9999'
                ORDER BY id ASC
            LOOP
                new_id := 'r' || LPAD(counter::TEXT, 4, '0');
                UPDATE request
                SET id = new_id
                WHERE id = temp_id;
                counter := counter + 1;
            END LOOP;
            new_id := 'r' || LPAD(counter::TEXT, 4, '0');
            UPDATE request
            SET id = new_id
            WHERE id = 'r9999';
        END IF;
        SELECT array_agg(id ORDER BY id) INTO new_ids
        FROM request
        WHERE id NOT LIKE 'r%';
        NEW.id := new_ids;
        RETURN NEW;
    END;
    $$;


ALTER FUNCTION public.update_request() OWNER TO postgres;

--
-- TOC entry 240 (class 1255 OID 34713)
-- Name: update_salary(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.update_salary() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    request_time VARCHAR(20);
    request_distance DOUBLE PRECISION;
    request_price NUMERIC;
    sum_employee DOUBLE PRECISION;
    count_car INT;
BEGIN
    IF NEW.status = 'Hoàn thành' THEN
        request_time := CONCAT('Tháng ', EXTRACT(MONTH FROM NEW."date"), ' ', EXTRACT(YEAR FROM NEW."date"));
        request_distance := NEW.distance;
        request_price := NEW.price;
        count_car := (SELECT COUNT(*) FROM car);
        IF EXISTS(SELECT * FROM salary WHERE "date" = request_time) THEN
            UPDATE salary 
            SET supplementary = (TRUNC(request_distance * 15000 / 1000) * 1000) + (SELECT supplementary FROM salary WHERE "date" = request_time),
                salary = (TRUNC(request_price * 0.3 / 1000) * 1000) + (SELECT salary FROM salary WHERE "date" = request_time),
                earning = request_price + (SELECT earning FROM salary WHERE "date" = request_time)
            WHERE "date" = request_time;
        ELSE
            sum_employee := (SELECT COUNT(*) FROM employee) * 15000000;
            count_car := (SELECT COUNT(*) FROM car) * 5000000;
            INSERT INTO salary (supplementary, salary, earning, "date")
            VALUES ((TRUNC(request_distance * 15000 / 1000) * 1000)+count_car, (TRUNC((sum_employee + request_price * 0.3) / 1000) * 1000), request_price, request_time);
        END IF;
    END IF;

    RETURN NEW;
END;
$$;


ALTER FUNCTION public.update_salary() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 215 (class 1259 OID 34714)
-- Name: account; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.account (
    id integer NOT NULL,
    username character varying(256),
    password character varying(256),
    account_type character varying(10)
);


ALTER TABLE public.account OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 34719)
-- Name: account_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.account_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.account_id_seq OWNER TO postgres;

--
-- TOC entry 3406 (class 0 OID 0)
-- Dependencies: 216
-- Name: account_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.account_id_seq OWNED BY public.account.id;


--
-- TOC entry 217 (class 1259 OID 34720)
-- Name: car; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.car (
    id character varying(256) NOT NULL,
    license character varying(20),
    brand character varying(256),
    maintenance integer,
    seatnumber integer,
    driver_id integer
);


ALTER TABLE public.car OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 34725)
-- Name: driver; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.driver (
    id integer NOT NULL,
    fullname character varying(256),
    gender character varying(3),
    email character varying(256),
    phone_number character varying(10),
    dob date,
    cccd character varying(50),
    address character varying(256),
    car_id character varying(256),
    status character varying(256)
);


ALTER TABLE public.driver OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 34730)
-- Name: employee; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.employee (
    employee_id character varying(10) NOT NULL,
    fullname character varying(256),
    gender character varying(3),
    email character varying(256),
    phone_number character varying(10),
    dob date,
    branch character varying(256),
    address character varying(256),
    cccd character varying(50)
);


ALTER TABLE public.employee OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 34735)
-- Name: log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.log (
    "time" time with time zone,
    date date,
    id integer,
    action character varying(256),
    log_id integer NOT NULL
);


ALTER TABLE public.log OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 34738)
-- Name: log_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.log_log_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.log_log_id_seq OWNER TO postgres;

--
-- TOC entry 3407 (class 0 OID 0)
-- Dependencies: 221
-- Name: log_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.log_log_id_seq OWNED BY public.log.log_id;


--
-- TOC entry 222 (class 1259 OID 34739)
-- Name: request_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.request_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.request_id_seq OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 34740)
-- Name: request; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.request (
    id character varying(256) DEFAULT concat('r', nextval('public.request_id_seq'::regclass)) NOT NULL,
    "time" time(6) without time zone,
    date date,
    departure character varying(256),
    arrival character varying(256),
    price numeric,
    customer_id integer,
    driver_id integer,
    distance double precision,
    status character varying(100),
    car_id character varying(256)
);


ALTER TABLE public.request OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 34746)
-- Name: requestview; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.requestview AS
 SELECT request.id,
    request."time",
    request.date,
    request.departure,
    request.arrival,
    request.price,
    request.customer_id,
    request.driver_id,
    request.distance,
    request.status,
    driver.fullname AS driver_name,
    car.brand,
    car.seatnumber
   FROM ((public.request
     JOIN public.driver ON ((request.driver_id = driver.id)))
     JOIN public.car ON ((driver.id = car.driver_id)))
  WHERE (request.customer_id = 1);


ALTER TABLE public.requestview OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 34751)
-- Name: salary; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.salary (
    supplementary numeric,
    salary numeric,
    earning numeric,
    date character varying(20) NOT NULL
);


ALTER TABLE public.salary OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 34756)
-- Name: user_information; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_information (
    id integer NOT NULL,
    fullname character varying(256),
    gender character varying(3),
    email character varying(256),
    phone_number character varying(10),
    dob date
);


ALTER TABLE public.user_information OWNER TO postgres;

--
-- TOC entry 3212 (class 2604 OID 34761)
-- Name: account id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.account ALTER COLUMN id SET DEFAULT nextval('public.account_id_seq'::regclass);


--
-- TOC entry 3213 (class 2604 OID 34762)
-- Name: log log_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log ALTER COLUMN log_id SET DEFAULT nextval('public.log_log_id_seq'::regclass);


--
-- TOC entry 3388 (class 0 OID 34714)
-- Dependencies: 215
-- Data for Name: account; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.account (id, username, password, account_type) VALUES (2, 'manager', 'manager', 'manager');
INSERT INTO public.account (id, username, password, account_type) VALUES (1, 'user', 'user', 'user');
INSERT INTO public.account (id, username, password, account_type) VALUES (3, 'driver', 'driver', 'driver');
INSERT INTO public.account (id, username, password, account_type) VALUES (11, 'quang123', '12345678', 'user');
INSERT INTO public.account (id, username, password, account_type) VALUES (17, 'longdriver', 'long1234', 'driver');
INSERT INTO public.account (id, username, password, account_type) VALUES (19, 'tranguser', '12345678', 'user');


--
-- TOC entry 3390 (class 0 OID 34720)
-- Dependencies: 217
-- Data for Name: car; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.car (id, license, brand, maintenance, seatnumber, driver_id) VALUES ('c004', '15B-14828', 'Vinfast', 3, 4, NULL);
INSERT INTO public.car (id, license, brand, maintenance, seatnumber, driver_id) VALUES ('c001', '29B-11409', 'Honda City', 1, 4, NULL);
INSERT INTO public.car (id, license, brand, maintenance, seatnumber, driver_id) VALUES ('c005', '26A-01992', 'Honda CR-V', 1, 7, NULL);
INSERT INTO public.car (id, license, brand, maintenance, seatnumber, driver_id) VALUES ('c003', '29K-17828', 'Ford Transit', 1, 7, NULL);
INSERT INTO public.car (id, license, brand, maintenance, seatnumber, driver_id) VALUES ('c007', '30K-19456', 'Vinslow', 0, 4, NULL);
INSERT INTO public.car (id, license, brand, maintenance, seatnumber, driver_id) VALUES ('c002', '30A-12212', 'Mercedes Benz', 1, 4, 3);
INSERT INTO public.car (id, license, brand, maintenance, seatnumber, driver_id) VALUES ('c006', '30T-41975', 'Kia Morning 2022', 0, 4, 17);


--
-- TOC entry 3391 (class 0 OID 34725)
-- Dependencies: 218
-- Data for Name: driver; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.driver (id, fullname, gender, email, phone_number, dob, cccd, address, car_id, status) VALUES (17, 'Phạm Hoàng Long', 'Nam', 'long.1234@gmail.com', '0337725612', '2001-07-14', '036203011140', 'Hoàng Cầu, Đống Đa, Hà Nội', 'c006', 'Không có đơn');
INSERT INTO public.driver (id, fullname, gender, email, phone_number, dob, cccd, address, car_id, status) VALUES (3, 'Phạm Thành Lập', 'Nam', 'phamthanhlap@gmail.com', '0915461657', '2003-02-07', '036203011139', 'Hoàng Mai, Hà Nội', 'c002', 'Không có đơn');


--
-- TOC entry 3392 (class 0 OID 34730)
-- Dependencies: 219
-- Data for Name: employee; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.employee (employee_id, fullname, gender, email, phone_number, dob, branch, address, cccd) VALUES ('nv0001', 'Phạm Thành Lập', 'Nam', 'phamthanhlap1@gmail.com', '0337726024', '2003-02-07', 'Marketing', 'Hoàng Mai, Hà Nội', '036203011140');
INSERT INTO public.employee (employee_id, fullname, gender, email, phone_number, dob, branch, address, cccd) VALUES ('nv0002', 'Phạm Thành Lập', 'Nữ', 'phamthanhlap2@gmail.com', '0337726030', '2003-02-07', 'Bảo vệ', 'Cầu Giấy, Hà Nội', '036203011137');
INSERT INTO public.employee (employee_id, fullname, gender, email, phone_number, dob, branch, address, cccd) VALUES ('nv0004', 'Phạm Thu Trang', 'Nam', 'thutrangpham@gmail.com', '0337726021', '2001-07-14', 'Tiếp tân', 'Trung Kính, Cầu Giấy, Hà Nội', '036201011125');
INSERT INTO public.employee (employee_id, fullname, gender, email, phone_number, dob, branch, address, cccd) VALUES ('nv0003', 'Phạm Thành Lập', 'Nam', 'phamthanhlap3@gmail.com', '0337726031', '2003-02-07', 'Nhân viên', 'Hà Đông, Hà Nội', '036203011114');


--
-- TOC entry 3393 (class 0 OID 34735)
-- Dependencies: 220
-- Data for Name: log; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('22:58:57+07', '2023-06-28', 2, 'Sửa thông tin xe có id = c003', 7);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('23:02:35+07', '2023-06-28', 2, 'Sửa thông tin xe có id = c003', 8);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('23:02:57+07', '2023-06-28', 2, 'Sửa thông tin xe có id = c003', 9);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('23:03:41+07', '2023-06-28', 2, 'Sửa thông tin xe có id = c003', 10);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('23:04:00+07', '2023-06-28', 2, 'Sửa thông tin xe có id = c003', 11);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('14:45:21+07', '2023-06-29', 2, 'Sửa thông tin khách hàng có id = 11', 12);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('14:45:41+07', '2023-06-29', 2, 'Sửa thông tin khách hàng có id = 11', 13);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('16:24:13+07', '2023-06-30', 2, 'Thêm xe có id = c  6', 15);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('16:24:29+07', '2023-06-30', 2, 'Xóa xe có mã = c  6', 16);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('16:26:12+07', '2023-06-30', 2, 'Thêm xe có id = c006', 17);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('17:55:31+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 18);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('17:56:15+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 19);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('17:57:02+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 20);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('17:57:24+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c003', 21);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('17:58:57+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c003', 22);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('18:00:16+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 23);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('18:01:28+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 24);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('18:01:56+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 25);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('18:03:15+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 26);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('18:03:29+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 27);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('18:04:01+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c003', 28);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('18:04:19+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 29);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('18:13:21+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 30);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('18:20:43+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 31);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('20:51:09+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c003', 32);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('20:55:26+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 33);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('20:56:34+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 34);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('20:56:50+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 35);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('20:56:59+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c003', 36);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('20:59:08+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c003', 37);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('21:00:25+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c003', 38);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('21:17:46+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 39);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('21:18:22+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 40);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('21:27:45+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 41);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('21:29:44+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c002', 42);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('21:34:09+07', '2023-06-30', 2, 'Sửa thông tin xe có id = c003', 43);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('12:10:25+07', '2023-07-01', 2, 'Sửa thông tin xe có id = c002', 44);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('12:10:43+07', '2023-07-01', 2, 'Sửa thông tin xe có id = c003', 45);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('12:12:02+07', '2023-07-01', 2, 'Sửa thông tin xe có id = c002', 46);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('12:44:40+07', '2023-07-01', 2, 'Thêm khách hàng có id = 16', 47);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('12:44:51+07', '2023-07-01', 2, 'Xóa khách hàng id = 16', 48);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('12:52:01+07', '2023-07-01', 2, 'Xóa tài xế id = 15', 49);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('12:53:39+07', '2023-07-01', 2, 'Thêm tài xế có id = 17', 50);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('12:54:07+07', '2023-07-01', 2, 'Xóa đơn hàng có mã = r0182', 51);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('12:54:46+07', '2023-07-01', 2, 'Xóa đơn hàng có mã = r0178', 52);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('12:54:57+07', '2023-07-01', 2, 'Xóa đơn hàng có mã = r0180', 53);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('12:55:06+07', '2023-07-01', 2, 'Xóa đơn hàng có mã = r0027', 54);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('12:55:18+07', '2023-07-01', 2, 'Xóa đơn hàng có mã = r0025', 55);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('12:56:00+07', '2023-07-01', 2, 'Thêm xe có id = c007', 56);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('12:57:32+07', '2023-07-01', 2, 'Thêm khách hàng có id = 18', 57);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('13:20:52+07', '2023-07-01', 2, 'Xóa khách hàng id = 18', 58);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('14:23:31+07', '2023-07-01', 2, 'Thêm khách hàng có id = 19', 59);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('10:30:22+07', '2023-07-08', 2, 'Sửa thông tin xe có id = c002', 60);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('10:30:54+07', '2023-07-08', 2, 'Sửa thông tin xe có id = c006', 61);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('10:31:15+07', '2023-07-08', 2, 'Xóa đơn hàng có mã = r0186', 62);
INSERT INTO public.log ("time", date, id, action, log_id) VALUES ('10:31:23+07', '2023-07-08', 2, 'Xóa đơn hàng có mã = r0176', 63);


--
-- TOC entry 3396 (class 0 OID 34740)
-- Dependencies: 223
-- Data for Name: request; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0004', '18:36:31', '2023-06-24', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c001');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0024', '02:37:16', '2023-06-29', 'Xuân Thủy, Cầu Giấy', 'Đại học bách khoa Hà Nội', 143000, 1, 3, 9.509, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0035', '03:30:20', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0016', '12:43:26', '2023-06-27', 'Hải Dương', 'Hải Phòng', 1043000, 1, 3, 69.476, 'Hoàn thành', 'c001');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0006', '10:39:53', '2023-06-26', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c001');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0017', '13:16:32', '2023-06-27', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Hoàn thành', 'c001');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0007', '10:53:25', '2023-06-26', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Hoàn thành', 'c001');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0001', '07:24:58', '2022-04-16', 'Đại học Bách Khoa HN, số 1 Đại Cồ Việt, Hai Bà Trưng, Hà Nội ', 'Vincom Mega Mall Royal City, 72A Nguyễn Trãi, Thượng Đình, Thanh Xuân, Hà Nội', 62000, 1, 3, 3.93, 'Hoàn thành', 'c001');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0026', '02:59:01', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0002', '08:44:05', '2022-04-27', 'Vincom Mega Mall Royal City, 72A Nguyễn Trãi, Thượng Đình, Thanh Xuân, Hà Nội', 'Đại học Bách Khoa HN, số 1 Đại Cồ Việt, Hai Bà Trưng, Hà Nội ', 55000, 1, 3, 3.29, 'Đã hủy', 'c001');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0018', '00:26:16', '2023-06-29', 'Xuân Trường, Nam Định', 'Hải Hậu, Nam Định', 278000, 1, 3, 18.509, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0013', '12:28:45', '2023-06-27', 'Nam Định', 'Ninh Bình', 486000, 1, NULL, 32.361, 'Đã hủy', NULL);
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0009', '11:17:30', '2023-06-27', 'Hà Nội', 'Hải Phòng', 1811000, 1, 3, 120.701, 'Đã hủy', 'c001');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0036', '03:31:56', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0019', '00:34:12', '2023-06-29', 'Xóm 7, Xuân Hòa , Xuân Trường, Nam Định', 'Trường Hải Hậu A', 241000, 1, 3, 16.008, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0028', '03:03:38', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0020', '00:46:29', '2023-06-29', 'Đình làng Mai Động , Hoàng Mai, Hà Nội', '85 Xuân Thủy, Cầu Giấy , Hà Nội', 210000, 1, 3, 13.976, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0012', '12:22:10', '2023-06-27', 'Nam Định', 'Hải Dương', 1136000, 1, 3, 75.729, 'Hoàn thành', 'c001');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0003', '09:44:17', '2023-07-25', 'Số 8 Phạm Ngọc Thạch, Quận Đống Đa, Hà Nội', 'Số 18 Trần Duy Hưng, Trung Hòa, Quận Cầu Giấy, Hà Nội', 73000, 1, 3, 5.37, 'Hoàn thành', 'c001');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0029', '03:08:03', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0021', '02:10:39', '2023-06-29', 'Xuân Thủy, Cầu Giấy', 'Đại học bách khoa Hà Nội', 143000, 1, 3, 9.509, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0022', '02:19:45', '2023-06-29', 'Xuân Thủy, Cầu Giấy', 'Đại học bách khoa Hà Nội', 143000, 1, 3, 9.509, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0030', '03:11:46', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0015', '12:30:31', '2023-06-27', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Hoàn thành', 'c001');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0023', '02:26:45', '2023-06-29', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0043', '03:45:55', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0037', '03:33:27', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0031', '03:15:39', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0038', '03:34:15', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0032', '03:17:28', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0033', '03:18:27', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0034', '03:20:31', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0051', '03:59:51', '2023-06-29', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0047', '03:54:02', '2023-06-29', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0039', '03:35:43', '2023-06-29', 'Cà Mau', 'Lạng Sơn', 32086000, 1, 3, 2139.046, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0040', '03:37:40', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0041', '03:39:09', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0044', '03:46:39', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0042', '03:40:32', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0048', '03:54:38', '2023-06-29', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0054', '04:04:24', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0045', '03:48:21', '2023-06-29', 'Hà Nội', 'Hải Phòng', 1811000, 1, 3, 120.701, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0046', '03:53:12', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 0, 84.415, 'Đã hủy', '');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0049', '03:56:33', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0052', '04:00:34', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0050', '03:57:35', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0053', '04:03:46', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0055', '04:05:20', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0056', '04:06:47', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0057', '04:07:21', '2023-06-29', 'Nam Định', 'Cà Mau', 29019000, 1, 3, 1934.568, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0058', '04:09:36', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0060', '04:11:11', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0059', '04:10:15', '2023-06-29', 'Hà Nội', 'Lạng Sơn', 2340000, 1, 3, 155.979, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0061', '04:22:35', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0062', '04:23:40', '2023-06-29', 'Nam Định', 'Hà Nam', 539000, 1, 3, 35.932, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0063', '04:27:29', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0064', '04:28:45', '2023-06-29', 'Hà Nội', 'Bắc Ninh', 620000, 1, 3, 41.268, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0065', '04:31:29', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0080', '05:16:27', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0066', '04:32:43', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0079', '05:15:44', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0067', '04:34:26', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0090', '14:12:18', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0068', '04:38:38', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0111', '05:45:21', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0069', '04:39:15', '2023-06-29', 'Sơn La', 'Hòa Bình', 3518000, 1, 3, 234.494, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0099', '23:39:24', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0091', '14:14:29', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0081', '05:17:02', '2023-06-29', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0070', '04:40:54', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0082', '05:17:48', '2023-06-29', 'Hà Nội', 'Hà Nam', 816000, 1, 3, 54.385, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0071', '04:41:24', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0092', '14:48:41', '2023-06-29', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0072', '04:48:33', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0083', '05:18:54', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0073', '04:49:19', '2023-06-29', 'Hòa Bình', 'Lạng Sơn', 3365000, 1, 3, 224.305, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0093', '14:56:22', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0084', '05:26:30', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0074', '05:00:16', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0075', '05:01:08', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0100', '23:44:23', '2023-06-29', 'Hà Nội', 'Hà Nam', 816000, 1, 3, 54.385, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0076', '05:02:19', '2023-06-29', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0085', '05:29:39', '2023-06-29', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0077', '05:10:18', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0094', '22:40:36', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0078', '05:12:49', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0086', '05:32:55', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0107', '05:34:50', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0101', '23:46:25', '2023-06-29', 'Hà Nội', 'Hà Nội', 0, 1, 3, 0, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0095', '22:50:20', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0087', '05:41:22', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0121', '06:09:18', '2023-06-30', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0088', '05:42:29', '2023-06-29', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0096', '23:03:24', '2023-06-29', 'Cầu Giấy, Hà Nội', 'Đại học bách khoa Hà Nội', 139000, 1, 3, 9.214, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0089', '14:07:39', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0102', '23:48:24', '2023-06-29', 'Hà Nội', 'Hà Nam', 816000, 1, 3, 54.385, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0115', '05:54:55', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0097', '23:09:06', '2023-06-29', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0108', '05:35:25', '2023-06-30', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0103', '00:08:17', '2023-06-30', 'Hà Nội', 'Hà Nội', 0, 1, 3, 0, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0098', '23:27:57', '2023-06-29', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0104', '03:48:02', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 0, 84.415, 'Đã hủy', '');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0112', '05:51:17', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0105', '03:49:39', '2023-06-30', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0109', '05:43:56', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0106', '03:58:29', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0110', '05:44:51', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0113', '05:52:33', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0118', '06:01:52', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0116', '05:56:07', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0114', '05:53:06', '2023-06-30', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0120', '06:03:51', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0117', '06:01:22', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0119', '06:02:56', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0122', '06:20:23', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0123', '06:21:23', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0125', '06:34:13', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0124', '06:31:14', '2023-06-30', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0126', '06:34:49', '2023-06-30', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0127', '06:39:03', '2023-06-30', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0128', '06:39:38', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0129', '06:44:00', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0130', '06:44:27', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0131', '06:46:55', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0157', '07:30:37', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0132', '06:47:20', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0147', '07:08:46', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0133', '06:48:44', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0134', '06:49:26', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0148', '07:11:09', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0135', '06:52:27', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0158', '07:31:26', '2023-06-30', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0136', '06:52:58', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0149', '07:11:38', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0137', '06:53:43', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0138', '06:54:11', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0174', '13:32:44', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0150', '07:14:22', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0139', '06:55:27', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0159', '07:32:40', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0140', '06:56:03', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0151', '07:14:52', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0141', '07:01:32', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0170', '12:29:24', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0142', '07:04:16', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0165', '12:08:21', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0152', '07:15:34', '2023-06-30', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0143', '07:04:49', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0144', '07:06:09', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0160', '11:51:44', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0145', '07:06:42', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0153', '07:17:58', '2023-06-30', 'Hà Nội', 'Hà Nam', 816000, 1, 3, 54.385, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0146', '07:08:19', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0154', '07:21:51', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0161', '11:58:11', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0155', '07:22:24', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Đã hủy', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0166', '12:09:25', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0156', '07:22:56', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0171', '12:40:11', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0162', '12:02:55', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0167', '12:13:51', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0163', '12:04:22', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0175', '13:38:05', '2023-06-30', 'Cà Mau', 'Bắc Kinh, Trung Quốc', 73543000, 1, 3, 4902.811, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0164', '12:07:01', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0168', '12:14:31', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0172', '12:44:57', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0169', '12:26:40', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0183', '17:52:49', '2023-06-30', 'Hà Nội', 'Bình Dương', 24068000, 1, 3, 1604.524, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0173', '13:22:49', '2023-06-30', 'Nam Định', 'Tây Tạng, Trung Quốc', 47870000, 1, 3, 3191.327, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0181', '17:51:31', '2023-06-30', 'Hà Nội', 'Bắc Ninh', 620000, 1, 3, 41.268, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0179', '17:44:17', '2023-06-30', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0177', '17:01:58', '2023-06-30', 'Hà Nội', 'Ninh Bình', 1412000, 1, 3, 94.133, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0188', '17:31:31', '2023-07-01', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0185', '13:15:42', '2023-07-01', 'Nam Định', 'Hà Nội', 1267000, NULL, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0184', '17:53:26', '2023-06-30', 'Hà Nội', 'Bình Định', 16073000, 1, 3, 1071.503, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0187', '16:18:30', '2023-07-01', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0189', '17:35:59', '2023-07-01', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0190', '17:39:52', '2023-07-01', 'Nam Định', 'Hà Nội', 1267000, 1, 3, 84.415, 'Hoàn thành', 'c002');
INSERT INTO public.request (id, "time", date, departure, arrival, price, customer_id, driver_id, distance, status, car_id) VALUES ('r0191', '23:57:09', '2023-07-10', 'Hà Nội', 'Nam Định', 1266000, 1, 3, 84.39, 'Đã hủy', 'c002');


--
-- TOC entry 3397 (class 0 OID 34751)
-- Dependencies: 225
-- Data for Name: salary; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.salary (supplementary, salary, earning, date) VALUES (5000000.00, 50000000.00, 150000000.00, 'Tháng 1 2023');
INSERT INTO public.salary (supplementary, salary, earning, date) VALUES (5063000, 61519000, 5067000, 'Tháng 7 2023');


--
-- TOC entry 3398 (class 0 OID 34756)
-- Dependencies: 226
-- Data for Name: user_information; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.user_information (id, fullname, gender, email, phone_number, dob) VALUES (1, 'Phạm Thành Lập', 'Nam', 'phamthanhlap2003.hha@gmail.com', '0337726028', '2003-02-07');
INSERT INTO public.user_information (id, fullname, gender, email, phone_number, dob) VALUES (19, 'Mai Thu Trang', 'Nữ', 'thutrang@gmail.com', '0337756140', '2003-05-04');


--
-- TOC entry 3408 (class 0 OID 0)
-- Dependencies: 216
-- Name: account_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.account_id_seq', 19, true);


--
-- TOC entry 3409 (class 0 OID 0)
-- Dependencies: 221
-- Name: log_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.log_log_id_seq', 63, true);


--
-- TOC entry 3410 (class 0 OID 0)
-- Dependencies: 222
-- Name: request_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.request_id_seq', 2, true);


--
-- TOC entry 3218 (class 2606 OID 34764)
-- Name: car Car_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.car
    ADD CONSTRAINT "Car_pkey" PRIMARY KEY (id);


--
-- TOC entry 3226 (class 2606 OID 34766)
-- Name: request Request_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request
    ADD CONSTRAINT "Request_pkey" PRIMARY KEY (id);


--
-- TOC entry 3216 (class 2606 OID 34768)
-- Name: account account_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.account
    ADD CONSTRAINT account_pkey PRIMARY KEY (id);


--
-- TOC entry 3220 (class 2606 OID 34770)
-- Name: driver driver_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.driver
    ADD CONSTRAINT driver_pkey PRIMARY KEY (id);


--
-- TOC entry 3222 (class 2606 OID 34772)
-- Name: employee employee_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.employee
    ADD CONSTRAINT employee_pkey PRIMARY KEY (employee_id);


--
-- TOC entry 3224 (class 2606 OID 34774)
-- Name: log log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log
    ADD CONSTRAINT log_pkey PRIMARY KEY (log_id);


--
-- TOC entry 3228 (class 2606 OID 34776)
-- Name: salary salary_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.salary
    ADD CONSTRAINT salary_pkey PRIMARY KEY (date);


--
-- TOC entry 3230 (class 2606 OID 34778)
-- Name: user_information user_information_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_information
    ADD CONSTRAINT user_information_pkey PRIMARY KEY (id);


--
-- TOC entry 3239 (class 2620 OID 34827)
-- Name: car car_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER car_trigger AFTER INSERT OR DELETE ON public.car FOR EACH ROW EXECUTE FUNCTION public.change_car();


--
-- TOC entry 3240 (class 2620 OID 34825)
-- Name: employee employee_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER employee_trigger AFTER INSERT OR DELETE ON public.employee FOR EACH ROW EXECUTE FUNCTION public.change_employee();


--
-- TOC entry 3243 (class 2620 OID 34824)
-- Name: salary employee_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER employee_trigger AFTER INSERT OR DELETE ON public.salary FOR EACH ROW EXECUTE FUNCTION public.change_employee();


--
-- TOC entry 3241 (class 2620 OID 34821)
-- Name: request request_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER request_trigger AFTER INSERT ON public.request FOR EACH ROW EXECUTE FUNCTION public.update_request();


--
-- TOC entry 3242 (class 2620 OID 34779)
-- Name: request salary_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER salary_trigger AFTER INSERT ON public.request FOR EACH ROW EXECUTE FUNCTION public.update_salary();


--
-- TOC entry 3244 (class 2620 OID 34822)
-- Name: salary salary_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER salary_trigger AFTER INSERT OR DELETE ON public.salary FOR EACH ROW EXECUTE FUNCTION public.update_salary();


--
-- TOC entry 3231 (class 2606 OID 34780)
-- Name: car car_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.car
    ADD CONSTRAINT car_fkey FOREIGN KEY (driver_id) REFERENCES public.driver(id) NOT VALID;


--
-- TOC entry 3235 (class 2606 OID 34785)
-- Name: request car_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request
    ADD CONSTRAINT car_id FOREIGN KEY (car_id) REFERENCES public.car(id) NOT VALID;


--
-- TOC entry 3236 (class 2606 OID 34790)
-- Name: request customer_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request
    ADD CONSTRAINT customer_id FOREIGN KEY (customer_id) REFERENCES public.user_information(id) NOT VALID;


--
-- TOC entry 3232 (class 2606 OID 34795)
-- Name: driver driver_fkey_car; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.driver
    ADD CONSTRAINT driver_fkey_car FOREIGN KEY (car_id) REFERENCES public.car(id) NOT VALID;


--
-- TOC entry 3233 (class 2606 OID 34800)
-- Name: driver driver_fkey_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.driver
    ADD CONSTRAINT driver_fkey_id FOREIGN KEY (id) REFERENCES public.account(id) NOT VALID;


--
-- TOC entry 3237 (class 2606 OID 34805)
-- Name: request driver_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request
    ADD CONSTRAINT driver_id FOREIGN KEY (driver_id) REFERENCES public.driver(id) NOT VALID;


--
-- TOC entry 3234 (class 2606 OID 34810)
-- Name: log fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.log
    ADD CONSTRAINT fkey FOREIGN KEY (id) REFERENCES public.account(id) NOT VALID;


--
-- TOC entry 3238 (class 2606 OID 34815)
-- Name: user_information user_information_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_information
    ADD CONSTRAINT user_information_fkey FOREIGN KEY (id) REFERENCES public.account(id) NOT VALID;


-- Completed on 2023-07-11 00:06:41

--
-- PostgreSQL database dump complete
--

