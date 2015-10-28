# Ratings Prediction
This is an assignment for Stanford CS46N to predict movie ratings. Given a set of training data, we must predict how a given set of users will rate the given movies. For more information, see here: http://web.stanford.edu/class/cs46n/assignments.htm

## How to run
Run the main function. To change what it's predicting or where it's predicting the data to, edit the main function in the Main class. To run the evolutionary algorithm to determine more weights, uncomment that line in the Main class.

## Strategies
When predicting a rating, we combine three factors: the average rating of that movie (including whether that user is on average more or less critical than the average user), how similar users rated that movie, how similar users rated similar movies, and how that user rated similar movies. The weights of these three factors can be experimentally determined by testing a wide range of values and seeing how well it does at predicting a subset of the training data.

Determining similar users and movies is done using a distance algorithm. For example, users with the same gender and age might have a distance of zero, whereas users with a different gender and the same age would have a distance of one. Applying the distance algorithm is done by taking all the movies/users with distance less than k. The weights of the factors in the distance algorithms and the cutoff distance will be experimentally determined.

We use an evolutionary algorithm to determine the weights.

## Results

Our evolutionary algorithm determined the following weights:
- The average rating for that movie plus whether that user is more or less critical than average: 2.57
- How similar users rated that movie: 0.38 
- How similar users rated similar movies: 0.00
- How that user rated similar movies: 0.00 
- Gender weight: 0.96 
- Age weight: 3.36
- How similar users have to be in order to be taken into account: 2.69 
- Genre weight: 2.37
- How similar movies have to be in order to be taken into account: 3.54

This implies a couple conclusions:

- Ratings for a movie vary comparatively little between different users
- Age is much more important than gender

## Technical
We're using JDK 1.8.0_60. Given that the body of data is relatively small, the information is currently held in memory rather than a database, though this may change.
