import React from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';

import UpdateMovieScreen from './screens/UpdateMovieScreen';
import AddMovieScreen from './screens/AddMovieScreen';
import ViewMoviesScreen from './screens/ViewMoviesScreen';

const RootStack = createStackNavigator();

const App = () => {
  return (
    <NavigationContainer>
      <RootStack.Navigator>
        <RootStack.Screen
          name="ViewMovies"
          component={ViewMoviesScreen}
          options={{
            title: 'Watchlist',
            headerStyle: {
              backgroundColor: '#fc7c41',
            },
            headerTitleStyle: {
              color: '#000000',
              fontSize: 26,
              alignSelf: 'center',
            },
          }}
        />
        <RootStack.Screen
          name="AddMovie"
          component={AddMovieScreen}
          options={{
            title: 'New Movie',
            headerStyle: {
              backgroundColor: '#fc7c41',
            },
            headerTitleStyle: {
              color: '#000000',
              fontSize: 24,
            },
          }}
        />
        <RootStack.Screen
          name="UpdateMovie"
          component={UpdateMovieScreen}
          options={{
            title: 'Update',
            headerStyle: {
              backgroundColor: '#fc7c41',
            },
            headerTitleStyle: {
              color: '#000000',
              fontSize: 24,
            },
          }}
        />
      </RootStack.Navigator>
    </NavigationContainer>
  );
};

export default App;
