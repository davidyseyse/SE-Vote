import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";

export const MAX_COMMENT_LENGTH = 200;

export default function Comment({
    comment,
    onCommentChange
}:{
    comment: string;
    onCommentChange: (value: string) => void;
}) {
    return (
        <Box>
        <Typography variant="h6" component="div" sx={{ color: 'text.secondary' }}>
            對本周的課程或報告有什麼心得與回饋嗎？歡迎下方留言讓我們知道
        </Typography>
            <Box sx={{ '& > :not(style)': { mt: 2, width: '100%' } }}>
                <TextField
                    id="outlined-basic"
                    label="comment"
                    variant="outlined"
                    autoComplete="off"
                    sx={{
                        '& .MuiInputBase-root': {
                            height: '56px', // 自定義高度
                        }
                    }}
                    value={comment}
                    onChange={(event) => {onCommentChange(event.target.value)}}
                    helperText={`${comment.length}/${MAX_COMMENT_LENGTH}`}
                    slotProps={{
                        htmlInput: {
                            maxLength: MAX_COMMENT_LENGTH,
                        },
                        formHelperText: {
                            sx: {
                                textAlign: 'right',
                                mx: 0,
                            },
                        },
                    }}
                />
            </Box>
        </Box>
    );
}
