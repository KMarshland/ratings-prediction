# Ratings Prediction
This is an assignment for Stanford CS46N to predict movie ratings. Given a set of training data, we must predict how a given set of users will rate the given movies. For more information, see here: http://web.stanford.edu/class/cs46n/assignments.htm

## Strategies
When predicting a rating, we combine three factors: the average rating of that movie, how similar users rated that movie, and how that user rated similar movies. The weights of these three factors can be experimentally determined by testing a wide range of values and seeing how well it does at predicting a subset of the training data.

Determining similar users and movies is done using a distance algorithm. For example, users with the same gender and age might have a distance of zero, whereas users with a different gender and the same age would have a distance of one. Applying the distance algorithm is done by taking all the movies/users with distance less than k. The weights of the factors in the distance algorithms and the cutoff distance will be experimentally determined.

Obviously, this strategy requires a large number (8 so far) of coefficients to be determined experimentally. It will likely take too much time to test every combination, so the best option might be to use an evolutionary algorithm to find the best values.  

### Possible improvements
- Check if the user is more critical than average. For example, if a user rates movies on average 0.5 stars lower than average, you might predict the average rating for a movie minus 0.5 stars.
- Instead of just how similar users rated that movie, check how similar users rated similar movies
- Improve the distance algorithm for movies, possibly taking into account the name


## Technical
We're using JDK 1.8.0_60. Given that the body of data is relatively small, the information is currently held in memory rather than a database, though this may change.
