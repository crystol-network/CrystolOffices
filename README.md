# CrystolOffices
This plugin was created for the services of the `CrystolNetwork` server, it is totally dedicated to your needs.

## Summary
* [Usage Examples](#usage-examples)
* [Contribution](#contributing)

## Usage Examples
* Here are some examples of how you can use the functions they have to offer.
```java
public class Example {

    private final OfficesServices officesServices;
    private final PlayerPermission playerPermission;
    private final GroupLoader groupLoader;

    {
        officesServices = OfficesServices.getInstance();
        playerPermission = officesServices.getPlayerPermission();
        groupLoader = officesServices.getGroupLoader();
    }

    public void exampleOne(){

        final Player examplePlayer = Bukkit.getPlayer("ExamplePlayer");
        final PlayerPermission.UserData user = playerPermission.getUser(examplePlayer);

        //Add group
        user.addGroup(groupLoader.getGroup("example"));

        //Remove group
        user.removeGroup(groupLoader.getGroup("example"));

        if (user.getLargestGroup().hasPermission("permission.admin")){
            examplePlayer.sendMessage("You are Admin.");
        }

        if (examplePlayer.hasPermission("permission.mod")){
            examplePlayer.sendMessage("You are Moderator.");
        }

    }

}
```

```java
public class Example {

    private final OfficesServices officesServices;
    private final PlayerPermission playerPermission;

    public Example(OfficesServices officesServices){
        this.officesServices = officesServices;
        playerPermission = new PlayerPermission(officesServices);
    }

    public void exampleTwo(){

        final OfflinePlayer examplePlayer = Bukkit.getOfflinePlayer("Pedrinho");
        final PlayerPermission.UserData user = playerPermission.getUser(examplePlayer);

        if(user.getLargestGroup().hasPermission("money.view")){
            if (examplePlayer.isOnline()){
                examplePlayer.getPlayer().sendMessage("You are online and allowed to see other people's money.");
            } else {
                final Plugin plugin = officesServices.getPlugin();// return plugin
                plugin.getServer().getConsoleSender().sendMessage("Pedrinho is allowed to see other people's money.");
            }
        }

        officesServices.getNetworkService().getPool().getResource(); //get Redis

        //Redis sender example
        final RedisSender redisSender = new RedisSender(officesServices.getNetworkService());
        redisSender.add("Teste");
        redisSender.add("HIHI");
        redisSender.send("ServerName");


        //Tab 'TAG' change example
        officesServices.getTabService().execute(new TabService.TabUpdate() {

            @Override
            public void onUpdate(TabService.TabFactory tabFactory) {
                tabFactory.reset();
                tabFactory.appendPrefix("Example");
                tabFactory.appendSuffix("Test");
            }

        });

    }

}
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)