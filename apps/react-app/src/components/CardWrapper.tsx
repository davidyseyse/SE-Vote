import { styled } from '@mui/material/styles';
import MuiCard from '@mui/material/Card';

const CardWrapper = styled(MuiCard)(({ theme }) => ({
    display: 'flex',
    flexDirection: 'column',
    alignSelf: 'center',
    width: '100%',
    padding: theme.spacing(4),
    gap: theme.spacing(2),

    // 玻璃效果
    backgroundColor: 'rgba(255, 255, 255, 0.1)', // 半透明白色
    backdropFilter: 'blur(10px)', // 背景模糊
    WebkitBackdropFilter: 'blur(10px)', // Safari 支援
    border: '1px solid rgba(255, 255, 255, 0.2)', // 細邊框
    borderRadius: theme.spacing(2), // 圓角

    // 調整陰影讓玻璃效果更明顯
    boxShadow:
        'rgba(255, 255, 255, 0.1) 0px 1px 0px 0px inset, rgba(0, 0, 0, 0.1) 0px 5px 15px 0px',

    [theme.breakpoints.up('sm')]: {
        width: '450px',
        minHeight: '45vh',
    },

    ...theme.applyStyles?.('dark', {
        backgroundColor: 'rgba(0, 0, 0, 0.4)', // 深色模式半透明黑色
        border: '1px solid rgba(255, 255, 255, 0.1)', // 更淡的邊框
        boxShadow:
            'rgba(255, 255, 255, 0.05) 0px 1px 0px 0px inset, rgba(0, 0, 0, 0.3) 0px 5px 15px 0px',
    }),
}));

export default CardWrapper;