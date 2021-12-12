import React,{useEffect} from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Alert from '@material-ui/lab/Alert';
import CircularProgress from '@material-ui/core/CircularProgress';
import Container from '@material-ui/core/Container';
import Copyright from '../components/Copyright';
import { useTranslation } from 'react-i18next';
import { Formik, Form } from 'formik';
import * as yup from 'yup';
import {userService} from '../_services/user.service';
import {authenticationService} from '../_services/authentication.service';

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(8),
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
    marginTop: theme.spacing(3),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}));

export default function RegisterPage(props) {
  const classes = useStyles();
  // eslint-disable-next-line
  const { t, i18n } = useTranslation();
  const RegisterSchema = yup.object().shape({
    name: yup.string()
      .min(2, t('name-size'))
      .max(16, t('name-size'))
      .required(t('required')),
    email: yup.string().email(t('email-invalid')).required(t('required')),
    password: yup.string()
      .matches('^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$', t('password-invalid'))
      .required(t('required'))
  });

  useEffect(() => {
    //already logged in
    if (authenticationService.currentUserValue !== null) {
      props.history.push("/home")
    }
  })

  return (
    <Container component="main" maxWidth="xs">
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component="h1" variant="h5">
          {t('sign-up')}
        </Typography>
        <Formik
          initialValues={{
            name:"",
            email: "",
            password: ""
          }}
          validationSchema={RegisterSchema}
          onSubmit={(values, actions)=> {
            userService.register(values.name, values.password, values.email).then(
              success => {
                actions.setStatus("success");
                setTimeout(function(){
                  authenticationService.login(values.email, values.password);
                  props.history.push("/subscription");
                }, 3000)
                
              },
              error => {
                actions.setStatus();
                actions.setSubmitting(false);
                var fields = error.errorDetail;
                var message = error.message
                if("email" in fields){
                  actions.setFieldError("email", message)
                }
                if("password" in fields){
                  actions.setFieldError("password", fields.password)
                }
                if("name" in fields){
                  actions.setFieldError("name", fields.name)
                }
              }
            )
          }}
        >
          {({ errors, status, handleChange, touched,isSubmitting }) => (
            <Form className={classes.form}>
              <Grid container spacing={2}>
                <Grid item xs={12}>
                  <TextField
                    error={errors.name && touched.name}
                    autoComplete="name"
                    name="name"
                    variant="outlined"
                    required
                    fullWidth
                    onChange={handleChange}
                    id="name"
                    label={t('name')}
                    autoFocus
                    helperText={
                      errors.name && touched.name
                        ? errors.name
                        : null
                    }
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    error={errors.email && touched.email}
                    variant="outlined"
                    required
                    fullWidth
                    onChange={handleChange}
                    id="email"
                    label={t('email')}
                    name="email"
                    autoComplete="email"
                    helperText={
                      errors.email && touched.email
                        ? errors.email
                        : null
                    }
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    error={errors.password && touched.password}
                    variant="outlined"
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
                        : errors.password
                    }
                  />
                </Grid>
              </Grid>
              <Button
                type="submit"
                fullWidth
                variant="contained"
                color="primary"
                className={classes.submit}
              >
                {isSubmitting 
                  ? <CircularProgress color="secondary"/>
                  : t('sign-up') }
              </Button>
              {status && 
                  <Alert severity="success">{t('sign-up-success')}</Alert>}
              <Grid container justify="flex-end">
                <Grid item>
                  <Link href="/login" variant="body2">
                    {t('have-account')}
                  </Link>
                </Grid>
              </Grid>
            </Form>
          )}
        </Formik>
      </div>
      <Box mt={5}>
        <Copyright />
      </Box>
    </Container>
  );
}