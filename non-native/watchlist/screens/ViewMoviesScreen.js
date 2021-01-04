import React from 'react';
import {
  FlatList,
  SafeAreaView,
  Text,
  TouchableOpacity,
  View,
  Alert,
} from 'react-native';
import styles from '../shared/stylesheet';

let MOVIES = [
  {
    id: 1,
    title: 'Casablanca',
    director: 'M.Curtiz',
    year: '1942',
    rating: 0,
  },
  {
    id: 2,
    title: 'Goodfellas',
    director: 'M.Scorsese',
    year: '1990',
    rating: 7.8,
  },
];
let nextId = 3;

export default class ViewMoviesScreen extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      data: MOVIES,
      refreshing: false,
    };
  }

  componentDidMount() {
    this._unsubscribe = this.props.navigation.addListener('focus', () => {
      this.loadMovies();
    });
  }

  componentWillUnmount() {
    this._unsubscribe();
  }

  getMovie(id) {
    for (let item of MOVIES) {
      if (item.id === id) {
        return item;
      }
    }
    return null;
  }

  removeMovie(id) {
    MOVIES = MOVIES.filter((item) => item.id !== id);
    this.setState({refreshing: true});
    this.setState({data: MOVIES});
    this.setState({refreshing: false});
  }

  removeMovieDialog(id) {
    Alert.alert(
        'Delete',
        'Are you sure you want to delete ' + this.getMovie(id).title + '?',
        [
          {
            text: 'CANCEL',
            style: 'cancel',
          },
          {text: 'OK', onPress: () => this.removeMovie(id)},
        ],
        {cancelable: false},
    );
  }

  loadMovies() {
    if (!this.props.route.params) {
      this.setState({refreshing: true});
      this.setState({data: MOVIES});
      this.setState({refreshing: false});
      }
    else {
      if (this.props.route.params.updating) {
        this.setState({refreshing: true});
        MOVIES = MOVIES.map((obj) => {
          if (obj.id === this.props.route.params.id){
            return {
              id: this.props.route.params.id,
              title: this.props.route.params.title,
              director: this.props.route.params.director,
              year: this.props.route.params.year,
              rating: this.props.route.params.rating,
            }
          }
          else {
            return obj;
          }
        });
        this.setState({data: MOVIES});
        this.setState({refreshing: false});
        this.props.route.params.updating = false;
      } else if (this.props.route.params.adding) {
        this.setState({refreshing: true});
        MOVIES.push({
          id: nextId,
          title: this.props.route.params.title,
          director: this.props.route.params.director,
          year: this.props.route.params.year,
          rating: this.props.route.params.rating,
        });
        nextId++;
        this.setState({data: MOVIES});
        this.setState({refreshing: false});
        this.props.route.params.adding = false;
      }
    }
  }


  renderMovieComponent = ({id, title, director, year, rating}) => {
    if (rating === 0) {
      return (
        <TouchableOpacity
          onLongPress={() => this.removeMovieDialog(id)}
          onPress={() =>
            this.props.navigation.navigate('UpdateMovie', {
              id,
              title,
              director,
              year,
              rating,
            })
          }>
          <View style={styles.displayContainer}>
            <Text style={styles.title}>{title}</Text>
            <Text style={styles.director}>{director}</Text>
            <Text style={styles.year}>{year}</Text>
            <Text style={styles.notSeen}>Not seen</Text>
          </View>
        </TouchableOpacity>
      );
    } else {
      return (
        <TouchableOpacity
          onLongPress={() => this.removeMovieDialog(id)}
          onPress={() =>
            this.props.navigation.navigate('UpdateMovie', {
              id,
              title,
              director,
              year,
              rating,
            })
          }>
          <View style={styles.displayContainer}>
            <Text style={styles.title}>{title}</Text>
            <Text style={styles.director}>{director}</Text>
            <Text style={styles.year}>{year}</Text>
            <Text style={styles.hidden}>{rating} /10</Text>
            <Text style={styles.rating}>{rating} /10</Text>
          </View>
        </TouchableOpacity>
      );
    }
  };

  ItemSeparator = () => (
    <View
      style={{
        height: 1,
        backgroundColor: 'rgba(0,0,0,1)',
      }}
    />
  );

  handleRefresh = () => {
    this.setState({refreshing: false}, () => {
      this.loadMovies();
    });
  };

  render() {
    return (
      <View style={styles.mainContainer}>
        <SafeAreaView>
          <FlatList
            data={this.state.data}
            renderItem={(item) => this.renderMovieComponent(item.item)}
            keyExtractor={(item) => item.id.toString()}
            ItemSeparatorComponent={this.ItemSeparator}
            refreshing={this.state.refreshing}
            onRefresh={this.handleRefresh}
          />
        </SafeAreaView>
        <TouchableOpacity
          style={styles.fab}
          onPress={() => this.props.navigation.navigate('AddMovie')}>
          <Text style={{fontSize: 36, color: '#000000'}}>+</Text>
        </TouchableOpacity>
      </View>
    );
  }
}
