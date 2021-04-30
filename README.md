<h1>Тестовое задание TOT Systems</h1>
<h2>О приложении</h2>
<p>Для написания данного REST API был использован Spring Web, Spring Data на основе Spring Boot. Build-система - Maven. База данных - PotgreSQL. Был использован Liquibase для инициализации таблиц.</p>
<p>Приложение поделено на 2 слоя - <b>Controller</b> и <b>Model</b>. Controller обрабатывает запросы, Model осуществляет работу с хранением данных. Работа с данными в свою очередь поделена на 2 слоя: <b>Repostory</b> и <b>Service</b>.</p>
<p>Controller, получив запрос, обращается к Service, передавая ему данные запроса. Service преобразует входные параметры, чтобы передать их Repository с запросом данных. Получив данные, Service при необходимости дополнительно обрабатывает их и возвращает Controller. Controller упаковывает получанные данные в ответ и возвращает пользователю.</p>

<h2>Инструкция по запуску</h2>
<h3>Сборка архива</h3>
<p>Для запуска приложения необходимо сперва собрать war-архив с помощью Maven. Для этого нужно исполнить следующую команду в корне проекта:</p>
<p><code>mvn clean package spring-boot:repackage</code></p>
<p>Архив будет создан в папке <b>/target</b></p>
<h3>Запуск</h3>
<p>Архив можно запустить с помощью команды</p>
<p><code>java -jar *название архива* *параметры*</code></p>
<p>передав при этом 3 параметра:<p>
<p><code>--spring.datasource.url=jdbc:postgresql:*url базы данных*</code></p>
<p><code>--spring.datasource.username=*имя пользователя, имеющего доступ к базе*</code></p>
<p><code>--spring.datasource.password=*пароль для этого пользователя*</code></p>
<p>После чего к приложению можно будет обратиться по адресу <code>localhost:8080</code></p>

<h2>Примеры запросов</h2>
<h3>POST /import</h3>
<p>Загружает данные из xml файлов в базу данных. Файлы должны находиться <b>в папке data в том же каталоге, что и запускаемый war-архив</b>. Импортирует из папки все файлы с именем, соответствующим регулярным выражениям <code>securities_[0-9]+.xml</code> и <code>history_[0-9]+.xml</code>. Файлы должны соостветствовать структуре представленных примеров.</p>

<h3>GET /securities</h3>
<p>Возвращает страницу ценных бумаг.</p>
<p>Request body: <code>{"pageableDTO": {"pageSize": 20, "pageNumber": 7}, "sortDTO": {"property": "emitentId", "direction": "ASC"}, "filterDTOs": {"emitentTitle": {"value": "The Boeing Company", "rule": "EQUALSORMORE"} } }</code>
<p>Поддерживает ильтрацию по полям <code>emitentTitle</code> и <code>regnumber</code>.</p>

<h3>GET /secirities/{secid}</h3>
<p>Пример: <code>localhost:8080/securities/AAPL</code></p>
<p>Возвращает информацию по одной ценной бумаге с указанным secid</p>

<h3>POST /securities</h3>
<p>Загружает информацию о ценной бумаге, переданную в запросе</p>
<p>Request body: <code>{"secid": "POM", "shortname": "PomChamp", "regnumber": "", "name": "Помпомпом26", "emitentId": 128821, "emitentTitle": "Spook", "emitentInn": "", "histories": [ { "boardid": "TQBR", "tradedate": "2021-03-18", "secid": "POM", "shortname": "PomChamp", "numtrades": 171.0, "value": 100.0, "open": 2.0, "low": 1.0, "high": 4.0, "close": 3.0 } ] }</code></p>

<h3>DELETE /securities/{secid}</h3>
<p>Удаляет информацию о ценной бумаге с указанным secid. У удаляемой бумаги не должно быть зависящих историй - их необходимо предварительно очистить.</p>

<h3>PUT /securities</h3>
<p>Изменяет информацию о ценной бумаге с указанным secid, изменяя данные полей на переданные в запросе</p>
<p>Request body: <code>{"secid": "POM", "shortname": "PomChamp", "regnumber": "", "name": "StacyJazz booff", "emitentId": 1281003, "emitentTitle": "Stacyy", "emitentInn": "" }</code></p>

<h3>GET securities/join</h3>
<p>Возвращает страницу записей с полями из security и history.</p>
<p>Request body: <code>{ "sortDTO": { "property": "secid", "direction": "DESC" }, "filterDTOs": { "emitentTitle": { "valueType": "STRING", "rule": "EQUALS", "value": "ПУБЛИЧНОЕ АКЦИОНЕРНОЕ ОБЩЕСТВО \"АКЦИОНЕРНАЯ ФИНАНСОВАЯ КОРПОРАЦИЯ \"СИСТЕМА\"" }, "tradedate": { "valueType": "DATE", "rule": "LESS", "value": "2020-04-16" } } }</code></p>
<p>Поддерживает ильтрацию по полям <code>emitentTitle</code> и <code>tradedate</code>.</p>

<h3>history/...</h3>
<p>Для историй по ценным бумагам поддерживаются аналогичные запросы: получение страницы историй, получение одной истории по id (GET /history/getOneById), загрузка одной истории, удаление истоии по id, изменение истории.</p>
<p>Отличие заключается в том, что первичный ключ истории - составной. Он содержит три поля <code>boardid</code>, <code>secid</code> и <code>tradedate</code>.</p>
<p>Эти поля нужно передать в request body. Пример: <code>{ "boardid": "TQBR", "tradedate": "2021-03-18T00:00:00.000+00:00", "secid": "POM" }</code></p>

<h3>DELETE /history/deleteBySecid/{secid}</h3>
<p>Удаляет все истории с переданным secid, т.е. истории принадлежащие конкретной бумаге. Также возвращает список удаленных историй. Стоит использовать данный метод для удаления зависящих историй перед удалением бумаги</p>
