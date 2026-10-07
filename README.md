# SE Form Webapp Documents for TAs
1. 用 VS-code 開啟並下載 Extension: Markdown Preview Enhanced
2. 右鍵點擊本文件的預覽視窗
3. 選擇 Markdown Preview Enhanced: Open preview to the side

即可開啟預覽模式舒舒服服的觀看此文件

### 檔案結構
app
│  docker-compose.yaml
│
├─config
│      application.yaml

## 每個學期可能要修改的參數

`Application.yaml`

```yaml
# ISSLab 除外的實驗室需要自己架設 DB
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/***
    username: ***
    password: ******

# 修改成正確的開學週的週一日期
date:
  semesterStartDay: 2025-09-01

# 如果評分有修改，可以直接在這裡調整
score: "{A: 100.0, B: 85.0, C: 70.0}"
```

## About Docker and App

### Pull Docker Image

1. 先通知 image 擁有者將 docker hub image 暫時開放為 public
2. Pull image
    - `na0105` : 應改為實際 image 擁有者的 docker hub 帳號
    - `se-form-webapp` : image 名稱
    - `latest` : 版本，應該為實際使用版本
    
    ```bash
    docker pull na0105/se-form-webapp:latest
    ```
    

### Start App with Container

- For TA from ISSLAB
    1. 切換 root 權限
        
        ```bash
        sudo su -
        ```
        
    2. 進入應用程式所在目錄
        
        ```bash
        cd /var/www/softwareEngineerWeb
        ```
        
    3. 執行 start-app.sh 腳本啟動應用程式
    
    ```bash
    . start-app.sh
    ```
    
    1. 確認 container 是否正常運行
        
        ```bash
        docker ps
        
        # 應看到以下訊息
        CONTAINER ID         IMAGE                              COMMAND                  CREATED        STATUS         PORTS                                       NAMES
        <app-container-id>   na0105/se-form-webapp:latest   "java -jar app.jar"      XX hours ago   Up X seconds   0.0.0.0:8080->8080/tcp, :::8080->8080/tcp   isslab-se-form-webapp
        <db-container-id>    postgres:14                        "docker-entrypoint.s…"   X days ago     Up X days      0.0.0.0:5432->5432/tcp, :::5432->5432/tcp   postgres-db
        ```
        
        - 如果沒有看到 app container 的資訊代表啟動失敗，查看 logs 排除問題
            
            ```bash
            docker ps -a # 可檢視包含未啟動的 container 資訊
            docker logs <app-container-id 前三碼>
            ```
            

- For other TAs
    1. 進入應用程式所在目錄
    2. 啟動 container
        
        ```bash
        docker-compose up -d app
        ```
        
    3. 確認 container 是否正常運行
        
        ```bash
        docker ps
        
        # 應看到以下訊息
        # CONTAINER ID         IMAGE                              COMMAND                  CREATED        STATUS         PORTS                                       NAMES
        # <app-container-id>   na0105/se-form-webapp:latest   "java -jar app.jar"      XX hours ago   Up X seconds   0.0.0.0:8080->8080/tcp, :::8080->8080/tcp   isslab-se-form-webapp
        # <db-container-id>    postgres:14                        "docker-entrypoint.s…"   X days ago     Up X days      0.0.0.0:5432->5432/tcp, :::5432->5432/tcp   postgres-db
        ```
        
        - 如果沒有看到 app container 的資訊代表啟動失敗，查看 logs 排除問題
            
            ```bash
            docker ps -a # 可檢視包含未啟動的 container 資訊
            docker logs <app-container-id 前三碼>
            ```
            

### Stop App with Container

1. 進入應用程式所在目錄
2. 停止應用程式
    1. For TA from ISSLAB：執行 stop-app.sh 腳本
        
        ```bash
        . stop-app.sh
        ```
        
    2. For other TAs
        
        ```bash
        docker-compose stop app
        ```
        
3. 檢查 container 運行狀況
    
    ```bash
    docker ps -a
    
    # 應看到以下訊息
    # CONTAINER ID         IMAGE                              COMMAND                  CREATED        STATUS                       PORTS                                       NAMES
    # <app-container-id>   na0105/se-form-webapp:latest   "java -jar app.jar"      XX hours ago   Exited (143) X seconds ago                                               isslab-se-form-webapp
    # <db-container-id>    postgres:14                        "docker-entrypoint.s…"   X days ago     Up X days                    0.0.0.0:5432->5432/tcp, :::5432->5432/tcp   postgres-db
    ```
    

### Remove App

如果要重新 pull 或是 image 有更新，則需要移除後再從 *Pull Docker Image* 操作一次

1. 進入應用程式所在目錄
2. 停止並移除 container
    1. For TA from ISSLAB：執行 remove-app.sh 腳本
        
        ```bash
        . remove-app.sh
        
        # 應該看以下訊息
        # Stopping isslab-se-form-webapp ... done
        # Going to remove isslab-se-form-webapp
        
        # Are you sure? [yN]
        # => 輸入 y
        
        # Removing isslab-se-form-webapp ... done
        ```
        
    2. For other TA
        
        ```bash
        docker-compose stop app
        # Stopping isslab-se-form-webapp ... done
        
        docker-compose rm app
        # Are you sure? [yN]
        # => 輸入 y
        
        # Removing isslab-se-form-webapp ... done
        ```
        

## About Configuration

### Application.yaml

1. Spring Boot 專案的主要設定檔，可用來指定應用程式的各種參數
2. 基本設定
    
    ```yaml
    spring:
      application:
        name: SE_form_backend  # 應用程式名稱
        
    server:
      port: 8080  # 伺服器啟動的 port
      address: 0.0.0.0  # 接受所有來自所有 IP 的連線
    ```
    
3. DB 相關
    
    ```yaml
    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/isslab  # DB server (ip:port/db name)
        username: ***
        password: ***
    
      jpa:
        hibernate:
          ddl-auto: none  # 設定 hibernate 啟動時如何處理 DB 的 schema
        show-sql: true  # 在 console print 出所有 JPA 執行的 SQL 語句
        properties:
          hibernate:
            dialect: org.hibernate.dialect.PostgreSQLDialect
    ```
    
4. Admin 權限
    
    ```yaml
    admin:
      username: # admin 帳號
      password: # admin 密碼
    ```
    
5. 課程相關變數設定
    
    ```yaml
    app:
      frontEnd:
        base-url: http://<isslab-web-server-ip>:8080/  # 網頁前端網址
    
    csv:
      output-path:  # 設定 CSV 輸出的路徑
        grades: <tmp>/backend-output/grades
        forms: <tmp>/backend-output/forms
        records: <tmp>/backend-output/records
    
    date:
      semesterStartDay: 2025-09-01  # 學期第一天
    
    score: "{A: 100.0, B: 85.0, C: 70.0}"  # 評分分數對照表
    ```
    

### Docker-compose.yaml

1. 應用程式相關設定
    
    ```yaml
    services:
      app:
        image: na0105/se-form-webapp:latest  # 網頁 image
        container_name: isslab-se-form-webapp
        ports:
          - "8080:8080"  # host_port : container_port
        depends_on:  # 見 2.
          - db
        volumes:
          - ./config:/app/config  # host config 路徑 : container 內路徑
          - ./output:/app/output  # host output 路徑 : container 內路徑
    ```
    
2. 用於執行 db 的設定檔
    
    ```yaml
    services:
    	db:
    	    image: postgres:14
    	    container_name: postgres-db
    	    restart: always
    	    environment:
    	      - POSTGRES_DB=isslab
    	      - POSTGRES_USER=***
    	      - POSTGRES_PASSWORD=***
    	    ports:
    	      - "5432:5432"
    	    volumes:
    	      - pgdata:/var/lib/postgresql/data  # host DB 資料映射路徑 : cotainer 內路徑
    	    security_opt:
    	      - apparmor=unconfined
    	      - seccomp=unconfined
    
    volumes:
      pgdata:
    ```
    

## API 呼叫流程

[完整 Swagger-api Document](https://drive.google.com/file/d/1kg-JuWvnqb7dJZLmZ2dv8MpyNu8f61Uf/view?usp=drive_link)，將內容複製到 [Swagger Editor](https://editor.swagger.io/) 查看

使用 postman 打以下的 api，已按照功能及順序儲存在 Collection **APIs For TA**

### 以管理員身份登入系統

`admin login` ：管理者帳號密碼可於 application.yaml 設定 (admin.username, admin.password)

1. Req params: 
    1. Header
        1. Content-Type: application/json
    2. Body
        1. LoginRequest
        
        ```json
        {
            "username": <admin.username>,
            "password": <admin.password>
        }
        ```
        

### 第一次正式上課之前

1. `import students by CSV` : 批次輸入多位學生
    1. Req params: 
        1. Header
            1. Authorization: Bearer {admin-token}
        2. File: csv，欄位如下
            
            
            | studentId | name | email | classType |
            | --- | --- | --- | --- |
            | 學號 | 姓名 | 信箱 | ON_SERVICE: 夜間部, DAY: 日間部 |
2. `import single student` : 事後加選的學生可以透過此 API 單獨輸入
    1. Req params: 
        1. Header
            1. Authorization: Bearer {admin-token}
            2. Content-Type: application/json
        2. Body
            1. Student
            2. `classType` ON_SERVICE（在職班）or DAY（日間部）
            
            ```json
            {
                "studentId": "113456789",
                "name": "Test",
                "email": "test@gmail.com",
                "classType": "DAY"
            }
            ```
            

### 每堂課開始前

1. `save presenters` : 紀錄當周所有報告者
    1. Req params: 
        1. Header
            1. Authorization: Bearer {admin-token}
            2. Content-Type: application/json
        2. Body
            1. List<Presenter>
            2. `presenterId` 報告者學號
            3. `presentWeek` 報告周次
            4. `presentOrder` 報告順序
            
            ```json
            [
                {
                    "presenterId": "113552XXX",
                    "presentWeek": "2",
                    "presentOrder": "1"
                },
                {
                    "presenterId": "113552XXX",
                    "presentWeek": "2",
                    "presentOrder": "2"        
                },
                ...
            ]
            ```
            
2. `get presenters by week` ：根據參數輸入的周次取得當周報告者資訊，用於檢查是否順利記錄報告者
    1. Req params: 
        1. Header
            1. Authorization: Bearer {admin-token}
        2. Path variable
            1. week: {week}
    2. Res:
        
        ```json
        
            {
                "presenterId": "114552XXX",
                "presentWeek": "2",
                "presentOrder": "1",
                "presnterName": "學生一",
                "participate": true
            },
            {
                "presenterId": "114552XXX",
                "presentWeek": "2",
                "presentOrder": "2",
                "presenterName": "學生二",
                "participate": true
            },
            ...
        ]
        ```
        

### 計算成績前

1. `export form submissions by week` : 根據參數輸入的周次匯出當周表單提交記錄
    1. 匯出路徑於 application.yaml 設定  (csv.output-path.forms)
    2. Req:
        1. Header:
            1. Authorization: Bearer {admin-token}
        2. Path variable:
            1. week: {week}
    3. Res:
        
        ```
        表單提交紀錄已匯出
        ```
        
2. `export score records by week` : 根據參數輸入的周次匯出當周表單評分記錄
    1. 匯出路徑於 application.yaml 設定  (csv.output-path.records)
    2. Req:
        1. Header:
            1. Authorization: Bearer {admin-token}
        2. Path variable:
            1. week: {week}
    3. Res:
        
        ```
        評分紀錄已匯出
        ```
        

### 成績計算

1. `save all grades by week` : 根據參數輸入的週次計算當周所有學生的成績（包含報告者與評分者）
    1. Req:
        1. Header:
            1. Authorization: Bearer {admin-token}
        2. Path variable:
            1. week: {week}
2. (opt.) `update presenter grade` : 手動更新報告者成績
    1. Req params: 
        1. Header
            1. Authorization: Bearer {admin-token}
            2. Content-Type: application/json
        2. Body
            1. Map<String ,String>
            
            ```json
            {
                "presenterId" : "113552XXX",
                "week": "2",
                "grade": 65
            }
            ```
            
3. (opt.) `update reviewer grade` : 手動更新評分者成績
    1. Req params: 
        1. Header
            1. Authorization: Bearer {admin-token}
            2. Content-Type: application/json
        2. Body
            1. Map<String ,String>
            2. `studentId` 評分者學號
            3. `presenterId` 對應的報告者學號
            
            ```json
            {
                "studentId" : "109502XXX",
                "presenterId": "113552XXX",
                "week": "1",
                "grade": 90
            }
            ```
            

### 確認同學參加情況（需於成績計算後才執行）

1. `1_check reviewer participants` ：檢查評分者是否有參與本週活動
    1. Req params: 
        1. Header
            1. Authorization: Bearer {admin-token}
        2. Path variable
            1. week: {week}
    2. Res:
        1. 所有人都參加活動
            
            ```json
            本周所有同學都有參加評分活動
            []
            ```
            
        2. 有人沒參加活動
            1. `classSkipped` ：該同學未參加活動次數，若大於 1 次則取消參加資格
            
            ```json
            本周有未參加評分的同學，已完成紀錄
            [
            	{
                "studentId": "113522XXX",
                "name": "學生一",
                "email": "113522XXX@cc.ncu.edu.tw", 
                "classType": "DAY",
                "password": null,
                "classSkipped": "1"
              },
              {
                "studentId": "113522XXX",
                "name": "學生二",
                "email": "113522XXX@cc.ncu.edu.tw", 
                "classType": "DAY",
                "password": null,
                "classSkipped": "1"
              },
              ...
            ]
            ```
            
2. (opt.) `set participants` ：根據參數輸入的報告者學號修改參加狀況
    1. Req param
        1. Header
            1. Authorization: Bearer {admin-token}
            2. Content-Type: application/json
        2. Body
            1. SetPresenterParticipateRequest
            2. `participate` : 該報告的周次有出席 → true，該報告卻未出席 → false
                
                ```json
                {
                    "presenterId": "113552015",
                    "week": "1",
                    "participate": false
                }
                ```
                
3. (opt.) `2_check presenter participants` ：檢查所有報告者是否參加報告
    1. Req params: 
        1. Header
            1. Authorization: Bearer {admin-token}
        2. Path variable
            1. week: {week}
4. `3_check reviewer eligibility` ：檢查是否有評分者（日間部同學）至少兩次未參加活動，達標就會被刪除
    1. Req params: 
        1. Header
            1. Authorization: Bearer {admin-token}
    2. Res:
        1. 無人達標
            
            ```json
            無學生超過翹課次數上限
            []
            ```
            
        2. 有人達標被刪除
            
            ```json
            有學生達兩次未參加評分，已被移除
            [
            	{
                "studentId": "113522XXX",
                "name": "學生一",
                "email": "113522XXX@cc.ncu.edu.tw", 
                "classType": "DAY",
                "password": null,
                "classSkipped": "2"
              },
              {
                "studentId": "113522XXX",
                "name": "學生二",
                "email": "113522XXX@cc.ncu.edu.tw", 
                "classType": "DAY",
                "password": null,
                "classSkipped": "2"
              },
              ...
            ]
            ```
            
5. (opt.) `delete student by id` ：根據參數輸入的學號手動刪除指定學生
    1. Req param
        1. Header
            1. Authorization: Bearer {admin-token}
            2. Content-Type: application/json
        2. Body
            1. Map<String ,String>
            
            ```json
            {
                "studentId" : "113552XXX"
            }
            ```
            

### 計算成績之後

1. `export presenter grades by week` ：根據參數輸入的周次匯出報告者成績
    1. 匯出路徑於 application.yaml 設定  (csv.output-path.grades)
    2. Req:
        1. Header:
            1. Authorization: Bearer {admin-token}
        2. Path variable:
            1. week: {week}
    3. Res:
        
        ```
        報告者成績檔已匯出
        ```
        
2. `export reviewer grades by week` ：根據參數輸入的周次匯出評分者成績
    1. 匯出路徑於 application.yaml 設定  (csv.output-path.grades)
    2. Req:
        1. Header:
            1. Authorization: Bearer {admin-token}
        2. Path variable:
            1. week: {week}
    3. Res:
        
        ```
        評分者成績檔已匯出
        ```
        

## 複製存在 ISSLab Web Server 的 CSV 檔至本機

若 Export 相關 API 與 ISSLab web server 啟動的 se-form-webapp server 互動，則可以透過以下指令取出：

```bash
 scp -r issl@<isslab-web-server-ip>:/var/www/softwareEngineerWeb/output <your-local-path>
```

或是可以修改 application.yaml 中的 datasource，在本機啟動相同的 se-form-webapp server 並且連接到 ISSLab Web Server 中啟用的 DB，CSV 將直接匯出到本機目錄：

```yaml
  datasource:
    url: jdbc:postgresql://<isslab-web-server-ip>:5432/isslab
    username: isslab
    password: ***
```