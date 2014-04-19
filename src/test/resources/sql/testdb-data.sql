--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.1
-- Dumped by pg_dump version 9.1.1
-- Started on 2014-04-03 11:56:09

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;

--
-- TOC entry 167 (class 3079 OID 11638)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 1877 (class 0 OID 0)
-- Dependencies: 167
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;


DROP TABLE public.country CASCADE;
DROP TABLE public.book CASCADE;
DROP TABLE public.author CASCADE;

--
-- TOC entry 161 (class 1259 OID 52430)
-- Dependencies: 6
-- Name: author; Type: TABLE; Schema: public; Owner: tester; Tablespace: 
--

CREATE TABLE author (
    id bigserial NOT NULL,
    name character varying(100),
    country bigint
)
WITH (
  OIDS=FALSE
);


ALTER TABLE public.author OWNER TO tester;

--
-- TOC entry 162 (class 1259 OID 52433)
-- Dependencies: 6
-- Name: book; Type: TABLE; Schema: public; Owner: tester; Tablespace: 
--

CREATE TABLE book (
    title character varying(100) NOT NULL,
    id bigserial NOT NULL,
    author bigint
)
WITH (
  OIDS=FALSE
);


ALTER TABLE public.book OWNER TO tester;

--
-- TOC entry 163 (class 1259 OID 52436)
-- Dependencies: 6
-- Name: country; Type: TABLE; Schema: public; Owner: tester; Tablespace: 
--

CREATE TABLE country (
    title character varying(32) NOT NULL,
    id bigserial NOT NULL
)
WITH (
  OIDS=FALSE
);


ALTER TABLE public.country OWNER TO tester;


--
-- TOC entry 1859 (class 2606 OID 52447)
-- Dependencies: 161 161
-- Name: author_pkey; Type: CONSTRAINT; Schema: public; Owner: tester; Tablespace: 
--

ALTER TABLE ONLY author
    ADD CONSTRAINT author_pkey PRIMARY KEY (id);


--
-- TOC entry 1865 (class 2606 OID 52449)
-- Dependencies: 163 163
-- Name: pk; Type: CONSTRAINT; Schema: public; Owner: tester; Tablespace: 
--

ALTER TABLE ONLY country
    ADD CONSTRAINT pk PRIMARY KEY (id);


--
-- TOC entry 1863 (class 2606 OID 52451)
-- Dependencies: 162 162
-- Name: pkey; Type: CONSTRAINT; Schema: public; Owner: tester; Tablespace: 
--

ALTER TABLE ONLY book
    ADD CONSTRAINT pkey PRIMARY KEY (id);


--
-- TOC entry 1860 (class 1259 OID 52452)
-- Dependencies: 161
-- Name: fki_ek; Type: INDEX; Schema: public; Owner: tester; Tablespace: 
--

CREATE INDEX fki_ek ON author USING btree (country);


--
-- TOC entry 1861 (class 1259 OID 52453)
-- Dependencies: 162
-- Name: fki_fkey author_id; Type: INDEX; Schema: public; Owner: tester; Tablespace: 
--

CREATE INDEX "fki_fkey author_id" ON book USING btree (author);


--
-- TOC entry 1866 (class 2606 OID 52454)
-- Dependencies: 163 1864 161
-- Name: ek; Type: FK CONSTRAINT; Schema: public; Owner: tester
--

ALTER TABLE ONLY author
    ADD CONSTRAINT ek FOREIGN KEY (country) REFERENCES country(id);


--
-- TOC entry 1867 (class 2606 OID 52459)
-- Dependencies: 1858 162 161
-- Name: fkey author_id; Type: FK CONSTRAINT; Schema: public; Owner: tester
--

ALTER TABLE ONLY book
    ADD CONSTRAINT "fkey author_id" FOREIGN KEY (author) REFERENCES author(id);


--
-- TOC entry 1865 (class 0 OID 52769)
-- Dependencies: 164
-- Data for Name: country; Type: TABLE DATA; Schema: public; Owner: tester
--

INSERT INTO "country" ("title", "id") VALUES ('Германия', 1);
INSERT INTO "country" ("title", "id") VALUES ('Дания', 2);
INSERT INTO "country" ("title", "id") VALUES ('Россия', 3);
INSERT INTO "country" ("title", "id") VALUES ('США', 4);


--
-- TOC entry 1863 (class 0 OID 52763)
-- Dependencies: 162
-- Data for Name: author; Type: TABLE DATA; Schema: public; Owner: tester
--

INSERT INTO "author" ("id", "name", "country") VALUES (131, 'Фаулер Мартин', 4);
INSERT INTO "author" ("id", "name", "country") VALUES (138, 'Christian Bauer', 4);
INSERT INTO "author" ("id", "name", "country") VALUES (141, 'Craig Walls', 4);
INSERT INTO "author" ("id", "name", "country") VALUES (143, 'Johann Goethe', 1);
INSERT INTO "author" ("id", "name", "country") VALUES (145, 'Hoeg Peter', 2);
INSERT INTO "author" ("id", "name", "country") VALUES (4, 'Гамма Эрих', 4);
INSERT INTO "author" ("id", "name", "country") VALUES (5, 'Lea Doug', 4);


--
-- TOC entry 1864 (class 0 OID 52766)
-- Dependencies: 163
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: tester
--

INSERT INTO "book" ("title", "id", "author") VALUES ('Concurrent programming in Java', 130, 5);
INSERT INTO "book" ("title", "id", "author") VALUES ('UML. Основы', 132, 131);
INSERT INTO "book" ("title", "id", "author") VALUES ('Рефакторинг. Улучшение существующего кода', 133, 131);
INSERT INTO "book" ("title", "id", "author") VALUES ('Приёмы объектно-ориентированного проектирования', 135, 4);
INSERT INTO "book" ("title", "id", "author") VALUES ('Hibernate in action', 139, 138);
INSERT INTO "book" ("title", "id", "author") VALUES ('Java persistence with Hibernate', 140, 138);
INSERT INTO "book" ("title", "id", "author") VALUES ('Spring in action, third edition', 142, 141);
INSERT INTO "book" ("title", "id", "author") VALUES ('Faust: der tragödie', 144, 143);
INSERT INTO "book" ("title", "id", "author") VALUES ('Den stille pige', 146, 145);
INSERT INTO "book" ("title", "id", "author") VALUES ('Froken Smillas fornemmelse for sne', 148, 145);


--
-- TOC entry 1876 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2014-04-03 12:16:20

--
-- PostgreSQL database dump complete
--


