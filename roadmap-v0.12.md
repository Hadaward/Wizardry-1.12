# Roadmap v0.12

### Changes
- [X] Migrate from LibLib's Config System to Forge's Config System.
  - The motivation for this migration is that currently Forge's configuration system is stable enough to use, and allows for the automated creation of a configuration GUI for the mod.
- [ ] Adding a volume category to the mod settings so players can have more control over what type of sound should be louder or quieter.
  - Greater sound control will now be offered in several categories: ``Entities``, ``Blocks``, ``Items``, ``User Interface`` and ``Spells``. In addition, the algorithm will now maintain the original volume of the sounds, only acting as a modulator between minimum (0) and maximum (original volume) through the percentage of the slider.
- [ ] The blacklist system for blocks that the ``Phase`` effect cannot affect will be reworked and revised to address issues found in 0.11.3 that could cause server crashes.

### Upcoming Features
- [ ] A new item called ``Crushed Unicorn Horn``.
      - There is no defined recipe yet, but one of the necessary items will be the bowl.
- [ ] A new item called ``Cursed Essence``.
      - The recipe for this item requires fairy dust and crushed unicorn horn.
- [ ] A new spell called ``Cursed Reflection``, entities under the effect of this spell receive a curse that redirects the negative effects of entities around them to themselves.
     - The catalyst for this spell will be the new item ``Cursed Essence``.

### Late Features
The features listed here are not guaranteed to be added in later 0.12 updates, so don't get your hopes up. They are also subject to change.
- A new block called ``Autocaster``, lets you put a infused nacre perl to auto cast spells.
   - Needs a new structure to be created
   - May need a new block without official name to serve as aim assist
   - Works with redstone
