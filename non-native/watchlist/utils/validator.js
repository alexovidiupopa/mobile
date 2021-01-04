export default function validate(title, director, year) {
  if (title && director && year) {
    return (
      title.toString() !== '' &&
      director.toString() !== '' &&
      year.toString() !== ''
    );
  } else {
    return false;
  }
}
