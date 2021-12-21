# Lab7: TLS configuration

## Весь проект:

[тык сюда](https://github.com/cyberg00se/chirpy-app)

## 1. Что сделано по TLS
Почитала [это](https://cheatsheetseries.owasp.org/cheatsheets/Transport_Layer_Protection_Cheat_Sheet.html). 

1. Желательная версия TLS - 1.3 (может использоваться и 1.2, но в 1.3 интересные плюшки: поддержка шифров понадёжнее (с аутентификацией), обмен ключами только по протоколу Диффи-Хеллмана с одноразовым ключом, etc).
2. Набор шифров дефолтный [как тут](https://nodejs.org/api/tls.html#modifying-the-default-tls-cipher-suite), TLS 1.3 может использовать оттуда 3 штуки: ```TLS_AES_256_GCM_SHA384```, ```TLS_CHACHA20_POLY1305_SHA256```, ```TLS_AES_128_GCM_SHA256```.
3. Используется HTTP Strict Transport Security для гарантии принудительного подключения по HTTPS, соответствующие заголовки в респонсы кидаются [вот этой](https://github.com/venables/koa-helmet) штукой ```koa-helmet```.

Конфиги лежат [тут](https://github.com/cyberg00se/chirpy-app/blob/main/chirpy-app-backend/src/config/default.js), используются [тут](https://github.com/cyberg00se/chirpy-app/blob/main/chirpy-app-backend/src/server.js). ```koa-helmet``` подключен [тут](https://github.com/cyberg00se/chirpy-app/blob/main/chirpy-app-backend/src/main.js).


## 2. Как сделаны и где лежат все штуки

В папке тут же на локалхосте есть закрытый ключ 4096 бит, self-signed сертификат (подписан своим же закрытым ключом, хотя можно было сделать root сертификат и потом уже с помощью него подписать сертификат для приложения) и файл с параметрами для обмена ключами Диффи-Хеллмана (для разных возможных случаев с TLS 1.2).

```bash
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -sha256 -days 365
openssl dhparam -out dhparam.pem 4096
```