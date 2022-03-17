/**
 * Pour rendre immuable la classe MoveEntity, nous avons utilisé la technique JPA consistant à
 * rendre les champs de classes en visibilité package, ce qui permet à JPA (Hibernate) d'accéder et
 * de modifier la valeur desdits champs .
 * 
 * La classe GameEntity est en charge d'instancier les nouvelles instances de la classe MoveEntity,
 * notamment via les fonctions GameEntity.makeMove.
 */
package io.github.oliviercailloux.assisted_board_games.resources;
