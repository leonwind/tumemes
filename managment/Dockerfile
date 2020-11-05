FROM postgres
ENV POSTGRES_DB tumemes
ENV POSTGRES_USER tumemes
ENV POSTGRES_PASSWORD $POSTGRES_PASSWORD
COPY createSchema.sql /createSchema.sql
COPY initialize.sh /docker-entrypoint-initdb.d
EXPOSE 5432
