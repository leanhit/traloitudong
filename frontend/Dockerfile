# syntax=docker/dockerfile:1
FROM node:20.9.0-alpine3.18 AS build

WORKDIR /app

COPY package*.json .

RUN npm install

COPY . .

RUN npm run build-only

FROM nginx:alpine

COPY --from=build /app/.nginx/nginx.conf /etc/nginx/conf.d/default.conf

WORKDIR /usr/share/nginx/html

RUN rm -rf ./*

COPY --from=build /app/dist .

ENTRYPOINT ["nginx", "-g", "daemon off;"]
