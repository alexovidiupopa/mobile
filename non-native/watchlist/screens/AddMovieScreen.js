import React from 'react';
import {
  Alert,
  Text,
  TextInput,
  TouchableOpacity,
  View,
} from 'react-native';
import Slider from '@react-native-community/slider';
import validate from '../utils/validator';
import styles from '../shared/stylesheet';

export default class AddMovieScreen extends React.Component {
  state = {
    title: null,
    director: null,
    year: null,
    rating: 5,
  };

  CustomSlider = () => {
    return (
      <View>
        <Slider
          style={{width: 400, height: 40}}
          minimumValue={0}
          step={0.5}
          onValueChange={(value) => this.setState({rating: value})}
          value={this.state.rating}
          maximumValue={10}
          minimumTrackTintColor="#000000"
          maximumTrackTintColor="#0000FF"
        />
        <Text style={{fontSize: 20, alignSelf: 'center'}}>
          {this.state.rating} /10
        </Text>
      </View>
    );
  };

  render() {
    return (
      <View style={styles.container}>
        <View style={styles.valueField}>
          <Text style={styles.textField}>Title:</Text>
          <TextInput
            editable
            style={styles.inputContainer}
            onChangeText={(value) => this.setState({title: value})}
            value={this.state.title}
          />
        </View>
        <View style={styles.valueField}>
          <Text style={styles.textField}>Director:</Text>
          <TextInput
            editable
            style={styles.inputContainer}
            onChangeText={(value) => this.setState({director: value})}
            value={this.state.director}
          />
        </View>
        <View style={styles.valueField}>
          <Text style={styles.textField}>Year:</Text>
          <TextInput
            editable
            style={styles.inputContainer}
            onChangeText={(value) => this.setState({year: value})}
            value={this.state.year}
          />
        </View>
        <View>{this.CustomSlider()}</View>
        <TouchableOpacity
          style={styles.fab}
          onPress={() => {
            if (
              validate(this.state.title, this.state.director, this.state.year)
            ) {
              this.props.navigation.navigate('ViewMovies', {
                adding: true,
                updating: false,
                title: this.state.title,
                director: this.state.director,
                year: this.state.year,
                rating: this.state.rating,
              });
            } else {
              Alert.alert('Invalid Data', "Please don't leave empty fields!");
            }
          }}>
          <Text style={{fontSize: 36, color: '#000000'}}>+</Text>
        </TouchableOpacity>
      </View>
    );
  }
}


