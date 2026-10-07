import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import api from "../../lib/axios.ts";
import {ROUTES} from "../../constants/routes.ts";

interface ForgotPasswordProps {
    open: boolean;
    handleClose: () => void;
    email: string;
}

export default function ForgotPassword({ open, handleClose, email }: ForgotPasswordProps) {
    return (
        <Dialog
            open={open}
            onClose={handleClose}
            slotProps={{
                paper: {
                    sx: { backgroundImage: 'none' },
                },
            }}
        >
            <DialogTitle><span>📧 Reset password </span></DialogTitle>
            <DialogContent
                sx={{ display: 'flex', flexDirection: 'column', gap: 2, width: '100%' }}
            >
                <DialogContentText>
                    請聯絡助教協助修改密碼：
                    {email}
                </DialogContentText>
            </DialogContent>
            <DialogActions sx={{ pb: 3, px: 3 }}>
                <Button onClick={handleClose}>Ok</Button>
            </DialogActions>
        </Dialog>
    );
}