import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { useTranslation } from 'react-i18next';
import clsx from 'clsx';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardMedia from '@material-ui/core/CardMedia';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Collapse from '@material-ui/core/Collapse';
import Button from '@material-ui/core/Button';
import Avatar from '@material-ui/core/Avatar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import { red } from '@material-ui/core/colors';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import MoreVertIcon from '@material-ui/icons/MoreVert';

const useStyles = makeStyles((theme) => ({
    
    media: {
        height: 0,
        paddingTop: '56.25%', // 16:9
    },
    expand: {
        transform: 'rotate(0deg)',
        marginLeft: 'auto',
        transition: theme.transitions.create('transform', {
            duration: theme.transitions.duration.shortest,
        }),
    },
    expandOpen: {
        transform: 'rotate(180deg)',
    },
    avatar: {
        backgroundColor: red[500],
    },
}));

export default function ArticleCard(props) {
    // eslint-disable-next-line
    const { t, i18n } = useTranslation();

    const article = props.article;

    const classes = useStyles();
    const [expanded, setExpanded] = React.useState(false);

    const handleExpandClick = () => {
        setExpanded(!expanded);
    };

    return (
        <Card>
            <CardHeader
                avatar={
                    <Avatar aria-label="source-name" className={classes.avatar}>
                        {article.source.name.slice(0,1)}
                    </Avatar>
                }
                title={article.title}
                subheader={t('by')+" "+article.source.name+" "+article.author}
            />
            {/*an article might not have an image sometimes...*/}
            {article.urlToImage !== null
                ? <CardMedia
                className={classes.media}
                image={article.urlToImage}
                title={article.title}/>
                : null
            }
            <CardContent>
                <Typography variant="body2" color="textSecondary" component={'span'}>
                    {article.description}
        </Typography>
            </CardContent>
            <CardActions disableSpacing>
                <Button size="small" color="primary" 
                onClick={()=> window.open(article.url, "_blank")}>
                    {t("read-more")}
                </Button>
                <IconButton
                    className={clsx(classes.expand, {
                        [classes.expandOpen]: expanded,
                    })}
                    onClick={handleExpandClick}
                    aria-expanded={expanded}
                    aria-label="show more"
                >
                    <ExpandMoreIcon />
                </IconButton>
            </CardActions>
            <Collapse in={expanded} timeout="auto" unmountOnExit>
                <CardContent>       
                    <Typography>
                        {article.content}
                    </Typography>       
                </CardContent>
            </Collapse>
        </Card>
    );
}
