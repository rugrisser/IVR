export class MenuItem {
  constructor(text, icon, link) {
    this.text = text
    this.icon = icon
    this.link = link
  }

  toJSON() {
    return JSON.stringify({
      text: this.text,
      icon: this.icon,
      link: this.link,
    })
  }
}

export async function getRole({ $axios }, token) {
  const response = await $axios.$get('/user/getRole', {
    headers: {
      Authorization: 'Bearer ' + token,
    },
  })
  const result = await response.name

  return result
}

export async function generatePanelMenu({ $axios }, token) {
  let result = []
  const role = await getRole({ $axios }, token)

  switch (role) {
    case 'admin':
      result = [
        new MenuItem('Новости', 'news.svg', '/panel/news'),
        new MenuItem('Пользователи', 'teamwork.svg', '/panel/users'),
        new MenuItem('Тикеты', 'siren.svg', '/panel/support'),
      ]
      break
    case 'deputy':
      result = [new MenuItem('Обращения', 'speaker.svg', '/appeals')]
      break
    case 'chairman':
      result = [
        new MenuItem('Обращения', 'speaker.svg', '/appeals'),
        new MenuItem('Пользователи', 'teamwork.svg', '/panel/users'),
      ]
      break
    default:
      break
  }

  result.push(new MenuItem('Выйти из ПУ', 'return.svg', '/news'))
  return result
}
