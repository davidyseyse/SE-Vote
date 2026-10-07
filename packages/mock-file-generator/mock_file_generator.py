import requests
import random
import time
from concurrent.futures import ThreadPoolExecutor
from config import token

# API URL
url = "http://localhost:8080/api/form-processing/"
PRESENTER_NUMBER = 8
MAX_WORKERS = 10       # 最大併發線程數

SCORE_OPTIONS = ["A", "B", "C"]

with open("./students/on_service.txt", "r") as f:
    PRESENTER_IDS = [line.strip() for _, line in zip(range(PRESENTER_NUMBER),f)]

# with open("./students/day.txt", "r") as f:
#     REVIEWER_IDS = [line.strip() for line in f]

# TOTAL_REQUESTS = PRESENTER_NUMBER * len(REVIEWER_IDS)  # 總共幾筆資料

# 產生單筆表單資料
def generate_form_data(week="1"):
    sampled_presenters = random.sample(PRESENTER_IDS, 8)
    return {
        "submitterId": "",
        "week": week,
        "scores": [
            {"score": random.choice(SCORE_OPTIONS), "presenterId": pid}
            for pid in sampled_presenters
        ],
        "submitDateTime": "20250808",
        "comment": "mock_file_generator",
    }

# 傳送一筆 request
def send_request():
    data = generate_form_data()
    headers = {
        "Authorization": f"Bearer {token}"
    }
    try:
        response = requests.post(url, json=data, headers=headers)
        print(f"Status: {response.status_code}")
    except Exception as e:
        print(f"Error: {e}")


# 執行主流程
def main():
    start = time.time()
    with ThreadPoolExecutor(max_workers=MAX_WORKERS) as executor:
        executor.map(lambda _: send_request(), range(1))
    end = time.time()
    print(f"✅ 共送出 {PRESENTER_NUMBER} 筆，耗時 {end - start:.2f} 秒")

if __name__ == "__main__":
    main()
