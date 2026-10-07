import Box from "@mui/material/Box";

export default function BackgroundWrapper({ children }: { children: React.ReactNode }) {
    return (
        <Box sx={(theme) => ({
            minHeight: '100dvh',
            backgroundImage: `
                radial-gradient(at 50% 50%, hsla(210, 100%, 97%, 0.2), hsla(0, 0%, 100%, 0.1)),
                url('/light-background.jpg')
            `,
            backgroundSize: 'cover',
            backgroundPosition: 'center',
            backgroundRepeat: 'no-repeat',
            ...theme.applyStyles?.('dark', {
                backgroundImage: `
                    radial-gradient(at 50% 50%, hsla(210, 100%, 16%, 0.55), hsla(220, 30%, 5%, 0.9)),
                    url('/dark-background.jpg')
                `,
            }),
        })}>
            {children}
        </Box>
    );
}