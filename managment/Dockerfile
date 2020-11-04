FROM postgres
ENV POSTGRES_DB tumemes
ENV POSTGRES_USER tumemes
ENV POSTGRES_PASSWORD test
COPY createSchema.sql /createSchema.sql
COPY initialize.sh /docker-entrypoint-initdb.d
EXPOSE 5432
