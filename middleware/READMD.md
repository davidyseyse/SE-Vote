## 啟動 postgres
先設定資料庫密碼，再啟動 PostgreSQL：

```powershell
$env:POSTGRES_PASSWORD = '<your-database-password>'
docker-compose up -d
```
