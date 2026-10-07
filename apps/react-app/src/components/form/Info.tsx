import * as React from 'react';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';
import Typography from '@mui/material/Typography';
import {Chip, Divider, Grid, Paper} from "@mui/material";
import ScoreSummary from "./ScoreSummary.tsx";
import SidePageWrapper from "../SidePageWrapper.tsx";

interface InfoProps {
    count: { A: number; B: number ; C: number };
    maxRatings: { A: number; B: number ; C: number };
    week: string;
}

export default function Info({count, maxRatings, week}: InfoProps) {
    const getChipColor = (grade: 'A' | 'B' | 'C', currentCount: number, maxRating: number) => {
        if (currentCount > maxRating) return 'error';
        if (currentCount === maxRating) return 'success';
        if (currentCount === 0) return 'warning';
        return 'success';
    };

    const totalRated = Object.values(count).reduce((sum, value) => sum + value, 0);

    return (
        <React.Fragment>
            {/*<SidePageWrapper>*/}
            <Typography variant="h2" sx={{ color: 'text.primary', my: 3 }}>
                Week {week} 報告評分
            </Typography>
            <Typography variant="h3" sx={{ color: 'text.secondary' }}>
                目前評分總計
            </Typography>
            <Divider orientation="horizontal" sx={{ my: 2 }} flexItem/>
            <ScoreSummary
                count={count}
                maxRatings={maxRatings}
                getChipColor={getChipColor}
            />
            <Divider orientation="horizontal" sx={{ my: 2 }} flexItem/>
            {/* 進度提示 */}
            <Paper sx={{
                p: 2,
                backgroundColor: 'transparent', // 透明背景
                color: 'info.contrastText',
                display: 'flex',
                justifyContent: 'flex-end'
            }}>
                <Typography variant="body2" textAlign="center">
                    已評分：{totalRated} 位同學
                </Typography>
            </Paper>
            {/*</SidePageWrapper>*/}
        </React.Fragment>
    );
}