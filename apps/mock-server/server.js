// mock-server/index.js
const express = require('express');
const app = express();
const PORT = 3000;

app.use(express.json());

app.post('/api/auth/login', (req, res) => {
    const { username, password } = req.body;
    console.log('✅ Received login:', { username, password });

    res.json({ token: 'mock-token-abc' });
});


app.get('/api/auth/me', (req, res) => {
    console.log("hello");

    res.json({ username: '111111' });
    // res.status(401);
});

app.get('/api/presenters', (req, res) => {
    //server 決定這周是week幾
    res.json({
        "week": "1",
        "presenters": [
            { "presenterId": "12552008", "name": "小明" },
            { "presenterId": "112552013", "name": "小美" },
            { "presenterId": "112552015", "name": "小張" }
        ]
    });
});

app.listen(PORT, () => {
    console.log(`🚀 Mock server is running at http://localhost:${PORT}`);
});