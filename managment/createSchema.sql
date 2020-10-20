--
-- PostgreSQL database dump
--

-- Dumped from database version 12.4
-- Dumped by pg_dump version 12.4

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: comments; Type: TABLE; Schema: public; Owner: tumemes
--

CREATE TABLE public.comments (
    commentid text NOT NULL,
    parentid text,
    memeid text NOT NULL,
    content text,
    author text,
    created timestamp without time zone
);


ALTER TABLE public.comments OWNER TO tumemes;

--
-- Name: commentvotes; Type: TABLE; Schema: public; Owner: tumemes
--

CREATE TABLE public.commentvotes (
    commentid text NOT NULL,
    username text NOT NULL,
    vote integer
);


ALTER TABLE public.commentvotes OWNER TO tumemes;

--
-- Name: memes; Type: TABLE; Schema: public; Owner: tumemes
--

CREATE TABLE public.memes (
    memeid character varying NOT NULL,
    title character varying,
    author character varying,
    created timestamp without time zone
);


ALTER TABLE public.memes OWNER TO tumemes;

--
-- Name: memevotes; Type: TABLE; Schema: public; Owner: tumemes
--

CREATE TABLE public.memevotes (
    memeid character varying,
    username character varying,
    vote integer
);


ALTER TABLE public.memevotes OWNER TO tumemes;

--
-- Name: users; Type: TABLE; Schema: public; Owner: tumemes
--

CREATE TABLE public.users (
    username character varying(32) NOT NULL,
    email character varying(64) NOT NULL,
    hash character varying(64) NOT NULL,
    salt character varying(64) NOT NULL
);


ALTER TABLE public.users OWNER TO tumemes;

--
-- Name: memes memes_pkey; Type: CONSTRAINT; Schema: public; Owner: tumemes
--

ALTER TABLE ONLY public.memes
    ADD CONSTRAINT memes_pkey PRIMARY KEY (memeid);


--
-- PostgreSQL database dump complete
--

