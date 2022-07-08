Все запросы методом Post.

Сохранить письма:
1. endpoint = /save_mails
2. Пример тела запроса
    [{\"id\":3,\"email\":\"2 serg@mail.ru\",\"recipient\":\"2 serg@mail.ru\",\"sender\":\"2 yakhin@mail.ru\",\"subject\":\"5 Test theme\",\"date\":\"2022-07-07T15:31\",\"calls\":[{\"id\":1,\"messageId\":3,\"status\":\"NEW\",\"phone\":\"89120338324\",\"callDate\":\"2022-07-07T15:31\"}]}]
    
Обновить имеющиеся письма (или сохранить новые):
1. endpoint = /update_mails
2. Пример тела запроса
    [{\"id\":3,\"email\":\"2 serg@mail.ru\",\"recipient\":\"2 serg@mail.ru\",\"sender\":\"2 yakhin@mail.ru\",\"subject\":\"5 Test theme\",\"date\":\"2022-07-07T15:31\",\"calls\":[{\"id\":1,\"messageId\":3,\"status\":\"NEW\",\"phone\":\"89120338324\",\"callDate\":\"2022-07-07T15:31\"}]}]

Запрос писем:
1. endpoint = /get_mails
2. Пример тела запроса
    {"sortField":null,"sortDir":null,"filters":[{"name":"email","value":"2 serg@mail.ru"},{"name":"startDate","value":"2022-07-07T15:31"}],"callFilters":[{"name":"phone","value":"89120338324"}]}
    
Удаление писем по фильтрам:
1. endpoint = /delete_mails
2. Пример тела запроса
    {"sortField":null,"sortDir":null,"filters":[{"name":"email","value":"2 serg@mail.ru"},{"name":"startDate","value":"2022-07-07T15:31"}],"callFilters":[{"name":"phone","value":"89120338324"}]}
    
    
    
