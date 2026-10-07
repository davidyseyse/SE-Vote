#!/bin/bash

# build-frontend.sh - 最簡單版本
echo "構建前端..."

# 獲取目錄路徑
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
FRONTEND_DIR="$SCRIPT_DIR/../react-app"
BACKEND_STATIC_DIR="$SCRIPT_DIR/../SE-form-backend/src/main/resources/static"

# 進入前端目錄
cd "$FRONTEND_DIR"

# 安裝 dependency
echo "安裝 dependency..."
npm ci

# 構建（跳過 TypeScript 檢查）
echo "構建前端..."
npx vite build

# 檢查是否構建成功
if [ ! -d "dist" ]; then
    echo "構建失敗"
    exit 1
fi

echo "構建完成"

# 複製到後端
echo "複製到後端..."
mkdir -p "$BACKEND_STATIC_DIR"
rm -rf "$BACKEND_STATIC_DIR"/*
cp -r dist/* "$BACKEND_STATIC_DIR/"

echo "完成"