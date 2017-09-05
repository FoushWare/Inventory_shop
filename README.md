# Inventory_shop

## Goals
* track inventory of products

## Operation:
* store information about 
    * Price
    * Quantity avaliable
    * Supplier
    * picture of product 

## User Stories
* Allow user to track 
    * sales
    * shipments
    * order more from suppliers

## Development [what i will do ]
* Sorting information in DB {SQlite}
* Presenting info for user From DB
* Update info based on user input 

##  Layout

 

### Main layout [launcher activity]
 * ListItem contains :
    * Product name
    * Current quantity
    * Price
    * Button to Reduce quantity by one {no negative value}
### Detail layout [Sub_Activity]

* For each item when clicked
    * Name
    * Current Quanitiy
    * Supplier
    * Button + Button - To increase and decrease the quantity
    * Button Delete to delete the item

### Functionality

* Runtime Error : if user enter invalid input {name ,quantity,image price } Not crash the App
But make Toast telling him to Enter valid info

* Delete Button need confirmation







