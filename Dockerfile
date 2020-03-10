FROM python:3.7-alpine
LABEL maintainer "Roman Matios <ucabrm0@ucl.ac.uk>"

COPY . /app
WORKDIR /app
RUN \
 apk add --no-cache bash && \
 apk add --no-cache --virtual .build-deps gcc musl-dev
RUN pip install -r requirements.txt
RUN rm -rf mysql-dev-db/ env/ flaskenv/
EXPOSE 5000
CMD ["/bin/bash", "entrypoint.sh"]