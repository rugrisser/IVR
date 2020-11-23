FROM node:14.15.0-alpine3.12

RUN mkdir -p /srv/app
COPY . /srv/app
WORKDIR /srv/app

COPY package*.json /srv/app

RUN yarn install
ENV NODE_ENV=production

COPY . /srv/app
RUN npm run build

EXPOSE 3000

ENV NUXT_HOST=0.0.0.0
ENV NUXT_PORT=3000

CMD ["yarn", "run", "start"]
