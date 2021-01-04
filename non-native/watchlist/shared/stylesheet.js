import {StyleSheet} from 'react-native';

const styles = StyleSheet.create({
    mainContainer: {
        flex: 1,
    },
    displayContainer: {
        display: 'flex',
        flexDirection: 'row',
        backgroundColor: '#FFF',
        paddingBottom: 5,
    },
    toolbar: {
        backgroundColor: '#fc7c41',
        height: 48,
        color: '#FFF',
        fontWeight: 'bold',
        fontSize: 27,
        paddingLeft: 15,
        paddingTop: 3,
    },
    leftColumn: {
        flex: 2,
    },
    rightColumn: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
    },
    director: {
        fontSize: 20,
        marginLeft: 25,
        marginTop: 4,
    },
    year: {
        fontSize: 16,
        fontStyle: 'italic',
        marginLeft: 25,
        marginTop: 5,
    },
    hidden: {
        fontSize: 0,
    },
    notSeen: {
        color: '#ff0008',
        fontWeight: 'bold',
        fontSize: 16,
        marginLeft: 25,
        marginTop: 4,
    },
    rating: {
        color: '#fc7c41',
        fontWeight: 'bold',
        fontSize: 16,
        marginLeft: 25,
        marginTop: 4,
    },
    container: {
        flex: 1,
    },
    valueField: {
        flexDirection: 'row',
    },
    inputContainer: {
        height: 40,
        width: 200,
        borderColor: 'gray',
        borderWidth: 1,
    },
    textField: {
        fontSize: 24,
    },
    fab: {
        borderWidth: 1,
        borderColor: 'rgba(0,0,0,0.2)',
        alignItems: 'center',
        justifyContent: 'center',
        width: 70,
        position: 'absolute',
        bottom: 10,
        right: 10,
        height: 70,
        backgroundColor: '#fc7c41',
        borderRadius: 100,
    },
});

export default styles;
