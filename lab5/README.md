# Lab5: Password storage. Hashing

> Your task is to create a simple client-server app (it may be just a web-app with front and back)
with client authentication. You should use the best practices of credential storage: strong hash,
salt, strong password requirements, honey pots etc. Everything that you find necessary for the
maximum password storage security.

## Есть API:

[тык сюда](https://github.com/cyberg00se/chirpy-app)

## Но фронта нет

🥺👉👈
есть тесты Postman

## По теме:
### 1. Хеширование паролей
Почитала [это](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html), сделала с алгоритмом bcrypt, который сделан на основе блочного шифра Blowfish. Раньше там было pbkdf2 с sha256, но это я раньше не знала, что есть разница в эффективности применения GPU для взлома (sha256 будет проще для GPU чем blowfish), та и толком параметры для pbkdf2 не подобрала (хотя с нормальными оно будет и по надёжности нормальное, больше итераций впихнуть).
~~Как я поняла, ещё лучше использовать Argon2id, потому что там нет шансов накосячить с параметрами.~~

bcrypt может быть слишком медленным для взломщика, и при этом достаточно быстрым для пользователя приложения (зависит от work factor). Work factor здесь 10, время не проверяла, но это примерно 10 хешей в секунду. Можно сделать больше, например, около 5 хешей/сек (чем больше work factor, тем дольше хешируется, сложнее взломать). Можно ещё больше, но тогда производительность приложения будет хуже.

[место в коде тык]( https://github.com/cyberg00se/chirpy-app/blob/44f9c3cb116a85262a7a3fcfa8f64129ec60ffbd/chirpy-app-backend/src/api/user/user.service.js#L52)

### 2. Что ещё
(за качество кода не бейте)

Какая-то минимальная тупая валидация пароля:
- длина от 10 до 25
- наличие хотя бы по одной штуке цифр, строчных букв, прописных букв и символов из набора !@#$%^&*

[код тут](https://github.com/cyberg00se/chirpy-app/blob/44f9c3cb116a85262a7a3fcfa8f64129ec60ffbd/chirpy-app-backend/src/api/user/user.middleware.js)
[и тут](https://github.com/cyberg00se/chirpy-app/blob/44f9c3cb116a85262a7a3fcfa8f64129ec60ffbd/chirpy-app-backend/src/api/auth/auth.middleware.js)
