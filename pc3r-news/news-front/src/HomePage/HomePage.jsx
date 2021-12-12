import React, { useState, useEffect } from 'react';
import ArticleCard from '../components/Article';
import { authenticationService } from '../_services/authentication.service';
import { subscriptionService } from '../_services/subscription.service'
import { articleService } from '../_services/article.service';
import { useTranslation } from 'react-i18next';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import PlaylistAddIcon from '@material-ui/icons/PlaylistAdd';
import Tooltip from '@material-ui/core/Tooltip';
import Badge from '@material-ui/core/Badge';
import MenuItem from '@material-ui/core/MenuItem';
import Menu from '@material-ui/core/Menu';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import AccountCircle from '@material-ui/icons/AccountCircle';
import MailIcon from '@material-ui/icons/Mail';
import NotificationsIcon from '@material-ui/icons/Notifications';
import MoreIcon from '@material-ui/icons/MoreVert';
import Avatar from '@material-ui/core/Avatar';

import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Box from '@material-ui/core/Box';
import { Container } from '@material-ui/core';

function TabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`scrollable-force-tabpanel-${index}`}
      aria-labelledby={`scrollable-force-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Container>
          <Box p={3}>
            {children}
          </Box>
        </Container>
      )}
    </div>
  );
}

function a11yProps(index) {
  return {
    id: `scrollable-force-tab-${index}`,
    'aria-controls': `scrollable-force-tabpanel-${index}`,
  };
}

const useStyles = makeStyles((theme) => ({
  grow: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(2),
  },
  title: {
    display: 'none',
    [theme.breakpoints.up('sm')]: {
      display: 'block',
    },
  },
  titleMobile: {
    display: 'flex',
    [theme.breakpoints.up('md')]: {
      display: 'none',
    },
    width: theme.spacing(3),
    height: theme.spacing(3)
  },
  sectionDesktop: {
    display: 'none',
    [theme.breakpoints.up('md')]: {
      display: 'flex',
    },
  },
  sectionMobile: {
    display: 'flex',
    [theme.breakpoints.up('md')]: {
      display: 'none',
    },
  },
}));

export default function HomePage(props) {
  const classes = useStyles();
  const [anchorEl, setAnchorEl] = React.useState(null);
  const [mobileMoreAnchorEl, setMobileMoreAnchorEl] = React.useState(null);

  const isMenuOpen = Boolean(anchorEl);
  const isMobileMenuOpen = Boolean(mobileMoreAnchorEl);

  const [value, setValue] = React.useState(0);
  const [subscriptions, setSubscriptions] = useState([]);
  const [articles, setArticles] = useState([]);
  const currentUser = authenticationService.currentUserValue;
  // eslint-disable-next-line
  const { t, i18n } = useTranslation();

  useEffect(() => {
    const fetchData = async () => {
      var getSubscriptions = await subscriptionService.getSubscriptions();
      var getArticles = await articleService.getArticles();
      setSubscriptions(getSubscriptions);
      setArticles(getArticles);
    };

    fetchData();
  }, [])


  function logout() {
    authenticationService.logout();
    props.history.push("/");
  }

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };


  const handleProfileMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMobileMenuClose = () => {
    setMobileMoreAnchorEl(null);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
    handleMobileMenuClose();
  };

  const handleMobileMenuOpen = (event) => {
    setMobileMoreAnchorEl(event.currentTarget);
  };

  const menuId = 'primary-search-account-menu';
  const renderMenu = (
    <Menu
      anchorEl={anchorEl}
      anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
      id={menuId}
      keepMounted
      transformOrigin={{ vertical: 'top', horizontal: 'right' }}
      open={isMenuOpen}
      onClose={handleMenuClose}
    >
      <MenuItem onClick={handleMenuClose}>Profile</MenuItem>
      <MenuItem onClick={handleMenuClose}>My account</MenuItem>
    </Menu>
  );

  const mobileMenuId = 'primary-search-account-menu-mobile';
  const renderMobileMenu = (
    <Menu
      anchorEl={mobileMoreAnchorEl}
      anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
      id={mobileMenuId}
      keepMounted
      transformOrigin={{ vertical: 'top', horizontal: 'right' }}
      open={isMobileMenuOpen}
      onClose={handleMobileMenuClose}
    >
      <MenuItem>
        <IconButton aria-label="log out" color="inherit" onClick={() => logout()}>
            <ExitToAppIcon /> {t('log-out')}
        </IconButton>
      </MenuItem>
      
    </Menu>
  );

  return (
    <div className={classes.grow}>
      <AppBar position="static" color="default">
        <Toolbar>

          <Typography className={classes.title} variant="h6" noWrap>
            News
          </Typography>
          <Avatar src="logo16.png" variant="square" className={classes.titleMobile} />
          <Tabs
            value={value}
            onChange={handleChange}
            variant="scrollable"
            scrollButtons="on"
            indicatorColor="primary"
            textColor="primary"
            aria-label="scrollable force tabs example"
          >
            {subscriptions && subscriptions.length
              ?
              subscriptions.map((category, i) => (
                <Tab key={i} label={category.name_category}  {...a11yProps(i)} />
              ))
              : null
            }
            <Tooltip title={t('add-category')} aria-label="add">
              <IconButton aria-label="add categories" color="inherit" onClick={() => props.history.push('/subscription')}> 
                <PlaylistAddIcon/>
              </IconButton>
            </Tooltip>
            
            
          </Tabs>
          <div className={classes.grow} />
          <div className={classes.sectionDesktop}>
            <IconButton aria-label="user name" color="inherit">
              <Typography>
                {t('welcome')+", "+currentUser.name}
              </Typography>
            </IconButton>
            <Tooltip title={t('log-out')} aria-label="log out">
              <IconButton aria-label="log out" color="inherit" onClick={() => logout()}>
                <ExitToAppIcon />
              </IconButton>
            </Tooltip>
          </div>
          <div className={classes.sectionMobile}>
            <IconButton
              aria-label="show more"
              aria-controls={mobileMenuId}
              aria-haspopup="true"
              onClick={handleMobileMenuOpen}
              color="inherit"
            >
              <MoreIcon />
            </IconButton>
          </div>
        </Toolbar>
      </AppBar>
      {renderMobileMenu}
      {renderMenu}
      <Container>
        {subscriptions && subscriptions.length
          ?
          subscriptions.map((category, i) => {
            const news = articles.filter((article, index) => (
              article.category.name_category === category.name_category
            ));
            return(
              <TabPanel key={i} value={value} index={i}> 
                {news.map((article, index) => (
                  <ArticleCard key={index} article={article} ></ArticleCard>
                ))}
              </TabPanel>
            );
            
          })
          : <div></div>
        }
      </Container>
    </div>
  );
}
