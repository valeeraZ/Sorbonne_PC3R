import React, { useEffect } from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Paper from '@material-ui/core/Paper';
import Box from '@material-ui/core/Box';
import Grid from '@material-ui/core/Grid';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import Alert from '@material-ui/lab/Alert';
import CircularProgress from '@material-ui/core/CircularProgress';
import { makeStyles } from '@material-ui/core/styles';
import Copyright from '../components/Copyright';
import { useTranslation } from 'react-i18next';
import { Formik, Form } from 'formik';
import * as yup from 'yup';
import { authenticationService } from '../_services/authentication.service'


const useStyles = makeStyles((theme) => ({
  root: {
    height: '100vh',
  },
  image: {
    backgroundImage: 'url(https://source.unsplash.com/random)',
    backgroundRepeat: 'no-repeat',
    backgroundColor:
      theme.palette.type === 'light' ? theme.palette.grey[50] : theme.palette.grey[900],
    backgroundSize: 'cover',
    backgroundPosition: 'center',
  },
  paper: {
    margin: theme.spacing(8, 4),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}));

export default function LoginPage(props) {
  const classes = useStyles();
  // eslint-disable-next-line
  const { t, i18n } = useTranslation();
  const LoginSchema = yup.object().shape({
    email: yup.string()
      .email(t('email-invalid'))
      .required(t('required')),
    password: yup.string()
      .required(t('required'))
  });

  useEffect(() => {
    //already logged in
    if (authenticationService.currentUserValue !== null) {
      props.history.push("/home")
    }
  })

  return (
    <Grid container component="main" className={classes.root}>
      <CssBaseline />
      <Grid item xs={false} sm={4} md={7} className={classes.image} />
      <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
        <div className={classes.paper}>
          <Avatar className={classes.avatar}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            {t('sign-in')}
          </Typography>
          <Formik
            initialValues={{
              email: "",
              password: ""
            }}
            validationSchema={LoginSchema}
            onSubmit={(values, actions) => {
              actions.setStatus();
              authenticationService.login(values.email, values.password).then(
                user => {
                  props.history.push("/home");
                },
                error => {
                  //console.log(error);
                  actions.setSubmitting(false);
                  actions.setStatus(error);
                }
              )
            }}
          >
            {({ errors, status, handleChange, touched, isSubmitting }) => (
              <Form className={classes.form} >
                <TextField
                  variant="outlined"
                  margin="normal"
                  required
                  fullWidth
                  onChange={handleChange}
                  id="email"
                  label={t('email')}
                  name="email"
                  autoComplete="username"
                  autoFocus
                  helperText={
                    errors.email && touched.email
                      ? errors.email
                      : null
                  }
                />
                <TextField
                  variant="outlined"
                  margin="normal"
                  required
                  fullWidth
                  onChange={handleChange}
                  name="password"
                  label={t('password')}
                  type="password"
                  id="password"
                  autoComplete="current-password"
                  helperText={
                    errors.password && touched.password
                      ? errors.password
                      : null
                  }
                />
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  color="primary"
                  className={classes.submit}
                >
                  {isSubmitting
                    ? <CircularProgress color="secondary" />
                    : t('sign-in')}
                </Button>

                {status &&
                  <Alert severity="error">{status.message}</Alert>}
                <Grid container>
                  <Grid item>
                    <Link href="register" variant="body2">
                      {t('no-account')}
                    </Link>
                  </Grid>
                </Grid>
              </Form>
            )}

          </Formik>
          <Box mt={5}>
            <Copyright />
          </Box>
        </div>
      </Grid>
    </Grid>
  );
}