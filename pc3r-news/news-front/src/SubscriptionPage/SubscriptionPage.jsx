import React, { useState, useEffect } from 'react';
import {
    makeStyles,
    Chip,
    Typography,
    Paper,
    Grid,
    Button,
    Avatar,
    Link,
    Box,
} from "@material-ui/core";
import Alert from '@material-ui/lab/Alert';
import SendIcon from '@material-ui/icons/Send';
import Copyright from '../components/Copyright';
import PlaylistAddIcon from '@material-ui/icons/PlaylistAdd';
import CircularProgress from '@material-ui/core/CircularProgress';
import { subscriptionService } from '../_services/subscription.service'
import { useTranslation } from 'react-i18next';
import { Formik, Form } from 'formik';

const useStyles = makeStyles((theme) => ({
    chipsDiv: {
        marginTop: theme.spacing(3),
    },
    chip: {
        margin: ".5rem",
        padding: "0.5rem",
    },
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
        margin: theme.spacing(20, 4),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    avatar: {
        margin: theme.spacing(1),
        backgroundColor: theme.palette.secondary.main,
    },
    submit: {
        margin: theme.spacing(3, 0, 2),
    },
    button: {
        margin: theme.spacing(1),
    }
}));

export default function SubscriptionPage(props) {
    const [subscriptions, setSubscriptions] = useState([])
    const [categories, setCategories] = useState([])
    useEffect(() => {
        const fetchData = async () => {
            var getCategories = await subscriptionService.getCategories();
            var getSubscriptions = await subscriptionService.getSubscriptions();
            setCategories(getCategories);
            setSubscriptions(getSubscriptions.map(category => {
                return category.id_category;
            }));
        };

        fetchData();
    }, [])

    const classes = useStyles();

    const handleClick = (clickedValue) => {
        if (subscriptions.find((e) => e === clickedValue)) {
            const index = subscriptions.findIndex((e) => e === clickedValue);
            let arr = [...subscriptions];
            arr.splice(index, 1);
            setSubscriptions(arr);
        } else {
            setSubscriptions([...subscriptions, clickedValue]);
        }
    };
    // eslint-disable-next-line
    const { t, i18n } = useTranslation();
    return (
        <Grid container component="main" className={classes.root}>
            <Grid item xs={false} sm={4} md={7} className={classes.image} />
            <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
                <div className={classes.paper}>
                    <Avatar className={classes.avatar}>
                        <PlaylistAddIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5">
                        {t('choose-categories')}
                    </Typography>
                    <Formik
                        initialValues= {{
                            value:'any'
                        }}
                        onSubmit={(values, actions) => {
                            actions.setStatus();
                            if (subscriptions.length !== 0){
                                subscriptionService.addSubscriptions(subscriptions).then(
                                    user => {
                                        //console.log(user)
                                        props.history.push("/home");
                                    },
                                    error => {
                                        //console.log(error);
                                        actions.setSubmitting(false);
                                        actions.setStatus(error);
                                    }
                                )
                            }else{
                                actions.setSubmitting(false);
                                var status = {"message": t('not-empty-sub')}
                                actions.setStatus(status);
                            }
                            
                        }}>
                        {({ errors, status, handleChange, touched, isSubmitting }) => (
                            <Form className={classes.chipsDiv}>
                                {categories && categories.length
                                    ? categories.map((category, i) => (
                                        <Chip
                                            icon={category.icon}
                                            className={classes.chip}
                                            key={i}
                                            color="primary"
                                            variant={
                                                subscriptions.find((e) => e === category.id_category)
                                                    ? "default"
                                                    : "outlined"
                                            }
                                            label={
                                                <Typography variant="body1">{`${category.name_category}`}</Typography>
                                            }
                                            clickable
                                            onClick={() => handleClick(category.id_category)}
                                        />
                                    ))
                                    : null}
                                <Button
                                    variant="contained"
                                    type="submit"
                                    color="primary"
                                    fullWidth
                                    className={classes.button}
                                    endIcon={isSubmitting ? null : <SendIcon></SendIcon> }
                                >
                                    {isSubmitting
                                        ? <CircularProgress color="secondary" />
                                        : <div>{t("submit-categories")}</div> }
                                </Button>
                                {status &&
                                    <Alert severity="error">{status.message}</Alert>}
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
