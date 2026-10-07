import { useEffect, useState } from 'react';
import api from "../lib/axios.ts";
import {Presenter} from "../types/presenter.ts";

export const usePresenters = () => {
    const [presenters, setPresenters] = useState<Presenter[]>([]);
    const [error, setError] = useState<string | null>(null);
    

    useEffect(() => {
        api.get(`/api/presenter/`)
            .then(res => {
                const filtered = res.data
                    .filter((p: Presenter) => p.participate)
                    .sort((a: Presenter, b: Presenter) => Number(a.presentOrder) - Number(b.presentOrder)); // 依照 presentOrder 排序
                setPresenters(filtered)
                console.log('res.data', res.data);
                // setPresenters(res.data)
            })
            .catch(err => {
                console.error('取得 presenter 失敗', err);
                setError('無法取得報告者資訊');
                setPresenters([]);
            });
    }, []);

    return { presenters, error };
};