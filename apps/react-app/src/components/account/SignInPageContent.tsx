import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';
import LoginIcon from '@mui/icons-material/Login';
import PersonAddAlt1 from '@mui/icons-material/PersonAddAlt1';
import ManageSearchIcon from '@mui/icons-material/ManageSearch';
import LockResetIcon from '@mui/icons-material/LockReset';

const items = [
    {
        icon: <LoginIcon sx={{ color: 'text.secondary' }} />,
        title: '請先登入',
        description: '投票前請先登入系統，以確保身分驗證與紀錄儲存。',
    },
    {
        icon: <PersonAddAlt1 sx={{ color: 'text.secondary' }} />,
        title: '第一次使用嗎？',
        description: '請以學號為帳號登入，初始密碼為000000。',
    },
    {
        icon: <LockResetIcon sx={{ color: 'text.secondary' }} />,
        title: '更改密碼',
        description: '為保護個人權益以及學分安全，首次登入後請更改密碼。',
    },
    {
        icon: <ManageSearchIcon sx={{ color: 'text.secondary' }} />,
        title: '評分審核機制',
        description: '系統會自動檢驗每位同學的評分分布，請務必遵守規則以避免退件。',
    },
];

export default function SignInPageContent() {
    return (
        <Stack
            sx={{ flexDirection: 'column', alignSelf: 'center', gap: 4, maxWidth: 450 }}
        >
            {items.map((item, index) => (
                <Stack key={index} direction="row" sx={{ gap: 2 }}>
                    {item.icon}
                    <div>
                        <Typography gutterBottom sx={{ fontWeight: 'medium' }}>
                            {item.title}
                        </Typography>
                        <Typography variant="body2" sx={{ color: 'text.secondary' }}>
                            {item.description}
                        </Typography>
                    </div>
                </Stack>
            ))}
        </Stack>
    );
}