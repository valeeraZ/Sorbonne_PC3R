import React from 'react';
import Link from '@material-ui/core/Link';
import Typography from '@material-ui/core/Typography';

export default function Copyright() {
    return (
      <Typography variant="body2" color="textSecondary" align="center">
        Sorbonne Université, M1 STL, 2020-2021 PC3R Project
        <br></br>
        {'Copyright © '}
        <Link color="inherit" href="https://gitlab.com/SylvainZhao/pc3r-news">
          News GitLab Repository
        </Link>{' '}
        {new Date().getFullYear()}
        {'.'}
      </Typography>
    );
  }